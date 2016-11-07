package com.rzagorski.adbtestrule;

import android.annotation.SuppressLint;

import com.rzagorski.adbtestrule.annotations.base.ADBOperation;
import com.rzagorski.adbtestrule.annotations.base.ExecutionDetails;
import com.rzagorski.adbtestrule.commands.ADBCommand;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Robert Zagórski on 2016-10-25.
 */

public class ADBTestRule implements TestRule {

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    @Override
    public Statement apply(Statement base, Description description) {
        Collection<Annotation> testMethodAnnotations = description.getAnnotations();
        if (testMethodAnnotations == null || testMethodAnnotations.size() == 0) {
            return evaluate(base);
        }
        List<ADBCommand> commandList = new ArrayList<>();
        for (Annotation testMethodAnnotation : testMethodAnnotations) {
            if (testMethodAnnotation == null) {
                continue;
            }
            Class<? extends Annotation> annotationClass = testMethodAnnotation.annotationType();
            if (annotationClass == null) {
                continue;
            }
            ADBOperation requiredAnnotation = annotationClass.getAnnotation(ADBOperation.class);
            if (requiredAnnotation == null) {
                continue;
            }
            ExecutionDetails executionDetailsAnnotation = annotationClass.getAnnotation(ExecutionDetails.class);
            Class<? extends ADBCommand> commandClass = executionDetailsAnnotation.executionClass();
            ADBCommand instance;
            try {
                Constructor commandClassConstructor = commandClass.getConstructor(annotationClass);
                instance = (ADBCommand) commandClassConstructor.newInstance(testMethodAnnotation);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to execute adb command:" + executionDetailsAnnotation.executionClass().getSimpleName(), e);
            }
            commandList.add(instance);
        }
        if (commandList.size() == 0) {
            return evaluate(base);
        } else {
            return new ADBStatement(commandList, base);
        }
    }

    public Statement evaluate(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }

    private class ADBStatement extends Statement {
        private final Statement mBase;
        private final List<ADBCommand> commandList;

        public ADBStatement(List<ADBCommand> commands, Statement mBase) {
            this.commandList = commands;
            this.mBase = mBase;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                for (ADBCommand command : commandList) {
                    command.execute();
                }
                mBase.evaluate();
            } finally {
                //TODO command.revert();
            }
        }
    }
}

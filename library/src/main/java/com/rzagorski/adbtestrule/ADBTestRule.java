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
import java.util.Collection;


/**
 * Created by Robert Zagórski on 2016-10-25.
 */

public class ADBTestRule<V extends ADBCommand> implements TestRule {

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    @Override
    public Statement apply(Statement base, Description description) {
        Collection<Annotation> testMethodAnnotations = description.getAnnotations();
        if (testMethodAnnotations == null || testMethodAnnotations.size() == 0) {
            return evaluate(base);
        }
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
            Class<?> commandClass = executionDetailsAnnotation.executionClass();
            V instance;
            try {
                Constructor commandClassConstructor = commandClass.getConstructor(annotationClass);
                instance = (V) commandClassConstructor.newInstance(testMethodAnnotation);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to execute adb command:" + executionDetailsAnnotation.executionClass().getSimpleName(), e);
            }
            return new ADBStatement(instance, base);
        }
        return evaluate(base);
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
        private final ADBCommand command;

        public ADBStatement(ADBCommand command, Statement mBase) {
            this.command = command;
            this.mBase = mBase;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                command.execute();
                mBase.evaluate();
            } finally {
                //TODO command.revert();
            }
        }
    }
}

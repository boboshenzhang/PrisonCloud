package top.ibase4j.core.util;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

public class ValidateUtil {

    public static <T> void validate(T obj) {
    	ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
				.buildValidatorFactory();
    	Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        //抛出检验异常
        Iterator<ConstraintViolation<T>> iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            ConstraintViolation<T> error = iter.next();
            StringBuffer buffer = new StringBuffer().append("[")
                    .append(error.getPropertyPath().toString()).append("]")
                    .append(error.getMessage());
            throw new IllegalArgumentException(buffer.toString());
        }
    }
}

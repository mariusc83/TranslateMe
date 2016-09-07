package org.mariusconstantin.translateme.persist.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by MConstantin on 8/23/2016.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerPersist {
}

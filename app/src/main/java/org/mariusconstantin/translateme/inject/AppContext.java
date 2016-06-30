package org.mariusconstantin.translateme.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by MConstantin on 7/4/2016.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface AppContext {

}

package org.mariusconstantin.translateme.persist;


import android.content.Context;

import org.junit.Before;
import org.mariusconstantin.translateme.inject.AppContext;
import org.mariusconstantin.translateme.inject.DaggerMockAppComponent;
import org.mariusconstantin.translateme.inject.MockAppComponent;
import org.mariusconstantin.translateme.inject.MockAppModule;
import org.mariusconstantin.translateme.persist.inject.DaggerMockPersistComponent;
import org.mariusconstantin.translateme.persist.inject.MockPersistComponent;
import org.mariusconstantin.translateme.persist.inject.MockPersistModule;
import org.mariusconstantin.translateme.persist.table.ITableBuilder;
import org.mariusconstantin.translateme.repositories.GoogleTokenRepo;
import org.mariusconstantin.translateme.repositories.misc.SharedPrefsRepo;
import org.mariusconstantin.translateme.utils.AppUtils;
import org.mariusconstantin.translateme.utils.ILogger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Created by MConstantin on 9/5/2016.
 */
public class DatabaseHelperTest {

    @Mock
    AppUtils mMockAppUtils;

    @AppContext
    @Mock
    Context mMockAppContext;

    @Mock
    GoogleTokenRepo mMockGoogleTokenRepo;

    @Mock
    ILogger mMockLogger;

    @Mock
    SharedPrefsRepo mMockSharedPrefsRepo;

    @Mock
    ITableBuilder mMockTableBuilder;

    DatabaseHelper mDatabaseHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockAppComponent mockAppComponent = DaggerMockAppComponent
                .builder()
                .mockAppModule(new MockAppModule(mMockAppContext,
                        mMockAppUtils,
                        mMockSharedPrefsRepo,
                        mMockLogger))
                .build();

        MockPersistComponent mockPersistComponent = DaggerMockPersistComponent
                .builder()
                .mockPersistModule(new MockPersistModule(mMockTableBuilder))
                .build();

        mDatabaseHelper = mockPersistComponent.getDatabaseHelper();
    }


}
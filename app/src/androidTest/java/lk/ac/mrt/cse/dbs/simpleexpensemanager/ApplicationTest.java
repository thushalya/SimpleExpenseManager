
/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;


import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.assertTrue;
import androidx.test.core.app.ApplicationProvider;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private ExpenseManager expenseManager;

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        DataBaseHelper dataBaseHelper=new DataBaseHelper(context);
        expenseManager = new PersistentExpenseManager(dataBaseHelper);
    }

    @Test
    public void testAddAccount(){
        expenseManager.addAccount("1234","BOC","Thushalya",1000);
        List<String> accountNumbers =expenseManager.getAccountNumbersList();
        assertTrue(accountNumbers.contains("1234"));

    }
}
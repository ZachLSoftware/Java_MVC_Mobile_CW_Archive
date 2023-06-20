package uk.ac.le.co2103.part2;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Root;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import android.app.Activity;
import android.view.View;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ShoppingListTest {

    private View decor;
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    //Clear shopping list DB
    @BeforeClass
    public static void nukeDB(){
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("shoppinglistdb");
    }


    @Test
    public void testAddNewList() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextListName)).perform(typeText("Birthday Party"));closeSoftKeyboard();
        onView(withText("Create")).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteList() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextListName)).perform(typeText("Birthday Party"));closeSoftKeyboard();
        onView(withText("Create")).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));
        onView(withText("Birthday Party")).perform(longClick());
        onView(withText("Confirm")).perform(click());
        onView(withText("Birthday Party")).check(doesNotExist());
    }


    @Test
    public void testAddProduct() {
        //Create List
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextListName)).perform(typeText("Birthday Party"));closeSoftKeyboard();
        onView(withText("Create")).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));

        //Get into birthday shopping list
        onView(withText("Birthday Party")).perform(click());

        //Add Product
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Cake"));
        closeSoftKeyboard();
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"));
        closeSoftKeyboard();

        //Open Spinner
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Unit"))).perform(click());

        //Confirm Spinner Selection
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Unit"))));
        onView(withId(R.id.addProductBtn)).perform(click());

        //Test Product added successfully
        onView(withText("Cake")).check(matches(isDisplayed()));
    }


    @Test
    public void testAddDuplicateProduct() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextListName)).perform(typeText("Birthday Party"));closeSoftKeyboard();
        onView(withText("Create")).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));
        onView(withText("Birthday Party")).perform(click());
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Cake"));
        closeSoftKeyboard();
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Unit"))).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Unit"))));
        onView(withId(R.id.addProductBtn)).perform(click());
        onView(withText("Cake")).check(matches(isDisplayed()));
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Cake"));
        closeSoftKeyboard();
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Unit"))).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Unit"))));
        onView(withId(R.id.addProductBtn)).perform(click());
        rule.getScenario().onActivity(activity -> {
            decor=activity.getWindow().getDecorView();
        });

        onView(withText(R.string.productError))
                .inRoot(withDecorView(not(decor)))
                .check(matches(isDisplayed()));
    }



    @Test
    public void testEditProduct() {
        //Create List
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextListName)).perform(typeText("Birthday Party"));closeSoftKeyboard();
        onView(withText("Create")).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));

        //Get into birthday shopping list
        onView(withText("Birthday Party")).perform(click());

        //Add Product
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Cake"));
        closeSoftKeyboard();
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"));
        closeSoftKeyboard();

        //Open Spinner
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Unit"))).perform(click());

        //Confirm Spinner Selection
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Unit"))));
        onView(withId(R.id.addProductBtn)).perform(click());

        //Test Product added successfully
        onView(withText("Cake")).check(matches(isDisplayed()));

        onView(withText("Cake")).perform(click());
        onView(withText("Edit")).perform(click());

        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.addBtn)).perform(click());
        onView(withId(R.id.updateProductBtn)).perform(click());

        onView(withText("3")).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteProduct() {
        //Create List
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextListName)).perform(typeText("Birthday Party"));closeSoftKeyboard();
        onView(withText("Create")).perform(click());
        onView(withText("Birthday Party")).check(matches(isDisplayed()));

        //Get into birthday shopping list
        onView(withText("Birthday Party")).perform(click());

        //Add Product
        onView(withId(R.id.fabAddProduct)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Cake"));
        closeSoftKeyboard();
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"));
        closeSoftKeyboard();

        //Open Spinner
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Unit"))).perform(click());

        //Confirm Spinner Selection
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Unit"))));
        onView(withId(R.id.addProductBtn)).perform(click());

        //Test Product added successfully
        onView(withText("Cake")).check(matches(isDisplayed()));

        onView(withText("Cake")).perform(click());
        onView(withText("Delete")).perform(click());

        //onView(withText(R.string.no_products)).check(matches(isDisplayed()));
        onView(withText("Cake")).check(doesNotExist());
    }

}
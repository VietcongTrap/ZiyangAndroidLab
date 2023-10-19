package algonquin.cst2355.huan0269.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import algonquin.cst2355.huan0269.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * This class is made to test the password validator
 * @author Ziyang Huang
 * @Version 1.0
 */
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * This test tests the result of entering all number password.
     */
    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView( withId(R.id.myEditText));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView( withId(R.id.myButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.myTextView));
        textView.check(matches(withText("You Shall NOT Pass")));
    }
    /**
     * This test tests the result of entering no uppercase password.
     */
    @Test
    public void testFindMissingUpperCase(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.myEditText));
        //set the passw
        appCompatEditText.perform(replaceText("password123#$*"));

        //find the button
        ViewInteraction materialButton = onView( withId(R.id.myButton));
        //click the button
        materialButton.perform(click());
        //find the textview
        ViewInteraction textView = onView(withId(R.id.myTextView));
        //check the result
        textView.check(matches(withText("You Shall NOT Pass")));
    }

    /**
     * This test tests the result of entering no lowercase password.
     */
    @Test
    public void testFindMissingLowerCase(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.myEditText));
        //set the passw
        appCompatEditText.perform(replaceText("PASS123#$*"));

        //find the button
        ViewInteraction materialButton = onView( withId(R.id.myButton));
        //click the button
        materialButton.perform(click());
        //find the textview
        ViewInteraction textView = onView(withId(R.id.myTextView));
        //check the result
        textView.check(matches(withText("You Shall NOT Pass")));
    }

    /**
     * This test tests the result of entering no number password.
     */
    @Test
    public void testFindMissingNumber(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.myEditText));
        //set the passw
        appCompatEditText.perform(replaceText("PASSword#$*"));

        //find the button
        ViewInteraction materialButton = onView( withId(R.id.myButton));
        //click the button
        materialButton.perform(click());
        //find the textview
        ViewInteraction textView = onView(withId(R.id.myTextView));
        //check the result
        textView.check(matches(withText("You Shall NOT Pass")));
    }


    /**
     * This test tests the result of entering no special character password.
     */
    @Test
    public void testFindMissingSpecialCharacter(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.myEditText));
        //set the passw
        appCompatEditText.perform(replaceText("PASSword123"));

        //find the button
        ViewInteraction materialButton = onView( withId(R.id.myButton));
        //click the button
        materialButton.perform(click());
        //find the textview
        ViewInteraction textView = onView(withId(R.id.myTextView));
        //check the result
        textView.check(matches(withText("You Shall NOT Pass")));
    }

    /**
     * This test tests the result of entering password with a digit, an upper case, a lower case, and a special character..
     */
    @Test
    public void testFindCorrectPassword(){
        //find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.myEditText));
        //set the passw
        appCompatEditText.perform(replaceText("PASSword123$$"));

        //find the button
        ViewInteraction materialButton = onView( withId(R.id.myButton));
        //click the button
        materialButton.perform(click());
        //find the textview
        ViewInteraction textView = onView(withId(R.id.myTextView));
        //check the result
        textView.check(matches(withText("Your pass word meets the requirements")));
    }
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

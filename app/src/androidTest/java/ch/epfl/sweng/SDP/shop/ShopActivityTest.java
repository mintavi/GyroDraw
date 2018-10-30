package ch.epfl.sweng.SDP.shop;

import android.os.CountDownTimer;
import android.os.Looper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;

import android.renderscript.Sampler;
import android.support.test.espresso.intent.Intents;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.support.test.espresso.matcher.ViewMatchers;

import static android.support.test.espresso.matcher.ViewMatchers.isClickable;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.widget.Button;
import android.widget.TextView;

import ch.epfl.sweng.SDP.Activity;
import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.home.HomeActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.Mock;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;

import org.mockito.internal.matchers.Null;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(AndroidJUnit4.class)
public class ShopActivityTest {
    private final int delayZero = 0;
    private final int delay500 = 500;
    private int stars;
    private final int validNewStars = 1000;
    private final int invalidNewStars = -1000;
    private final long longInt = 500;
    private final String testString = "testString";
    private final String color0 = "black";

    private HashMap<String, Boolean> userColors = new HashMap<>();



    @Mock
    private DatabaseReference currentUser;
    @Mock
    private DatabaseReference currentUserStars;
    @Mock
    private DatabaseReference currentUserItems;
    @Mock
    private DatabaseReference currentUserColors;
    @Mock
    private DatabaseReference currentUserSpecificItem;
    @Mock
    private DatabaseReference shopColors;
    @Mock
    private DatabaseReference shopColorsSpecificColor;

    @Mock
    private DataSnapshot nonExistant;
    @Mock
    private DataSnapshot longIntegerValueSnapshot;

    @Before
    public void setup() {

        //Initialize mocked objects.
        MockitoAnnotations.initMocks(this);

        when(currentUser.child("stars")).thenReturn(currentUserStars);
        when(currentUser.child("items")).thenReturn(currentUserItems);

        when(currentUserItems.child("colors")).thenReturn(currentUserColors);

        when(currentUserColors.child(anyString())).thenReturn(currentUserSpecificItem);

        when(currentUserStars.setValue(anyInt(), any())).thenAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) {
                stars = invocation.getArgument(1);
                assertEquals((int)invocation.getArgument(1), stars);
                return null;
            }
        });

        when(currentUserSpecificItem.setValue(eq(true), any()))
                .thenAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) {
                userColors.put(color0, true);
                assertTrue(userColors.get(color0));
                return null;
            }
        });

        when(shopColors.child(anyString())).thenReturn(shopColorsSpecificColor);


        /**
         * doAnswer(new Answer<Void>() {
         *
         *             @Override
         *             public Void answer(InvocationOnMock invocation) {
         *
         *                 return null;
         *             }
         *         }).when(shopColorsSpecificColor.addListenerForSingleValueEvent(any(ValueEventListener.class).onDataChange(isA(DataSnapshot.class))));
         */

        when(nonExistant.exists()).thenReturn(false);

        when(longIntegerValueSnapshot.exists()).thenReturn(true);
        when(longIntegerValueSnapshot.getValue()).thenReturn(longInt);

    }

    @Rule
    public final ActivityTestRule<ShopTestActivity> activityTestRule =
            new ActivityTestRule<>(ShopTestActivity.class);

    //relevant tests

    //tests for initializeReferences()

    //tests for onCreate()

    //tests for getColorsFromDatabase()

    //tests for initializeButton()

    @Test
    public void initializeButtonWorks() {
        Button btn = activityTestRule.getActivity().initializeButton(testString);
        assertEquals(testString, btn.getText());
        assertTrue(btn.isClickable());
    }

    //tests for addPurchaseOnClickListenerToButton()

    @Test
    public void addPurchaseOnClickListenerToButtonWorks() {
        Button btn = activityTestRule.getActivity().initializeButton(testString);
        assertFalse(btn.hasOnClickListeners());
        activityTestRule.getActivity().addPurchaseOnClickListenerToButton(btn);
        assertTrue(btn.hasOnClickListeners());
    }

    //tests for setReturn()

    @Test
    public void setReturnWorks() {
        Button btn = activityTestRule.getActivity().initializeButton(testString);
        assertFalse(btn.hasOnClickListeners());
        activityTestRule.getActivity().setReturn(btn);
        assertTrue(btn.hasOnClickListeners());
    }

    @Test
    public void setReturnReturnsToHomeOnClick() {
        Button btn = activityTestRule.getActivity().initializeButton(testString);
        activityTestRule.getActivity().setReturn(btn);
        Intents.init();
        btn.performClick();
        intended(hasComponent(HomeActivity.class.getName()));
        Intents.release();
    }

    //tests for setRefresh()

    @Test
    public void setRefreshWorks() {
        Button btn = activityTestRule.getActivity().initializeButton(testString);
        assertFalse(btn.hasOnClickListeners());
        activityTestRule.getActivity().setRefresh(btn);
        assertTrue(btn.hasOnClickListeners());
    }

    @Test
    public void setRefreshRefreshesOnClick() {
        Button btn = activityTestRule.getActivity().initializeButton(testString);
        activityTestRule.getActivity().setRefresh(btn);
        Intents.init();
        btn.performClick();
        intended(hasComponent(ShopTestActivity.class.getName()));
        Intents.release();
    }

    //tests for purchaseItem()

    //tests for alreadyOwned()

    //tests for updateUserIf()

    //tests for getStars()

    //tests for getPrice()

    @Test
    public void getPriceWorks() {

    }

    //tests for wrapDataSnapshotValue()

    @Test
    public void wrapDataSnapshotValueWorks() {
        IntegerWrapper wrapper = new IntegerWrapper(0);
        activityTestRule.getActivity().wrapDataSnapshotValue(longIntegerValueSnapshot, wrapper);
        assertEquals(longInt, wrapper.getInt());
    }

    @Test
    public void wrapDataSnapshotValueWorksWithNonExistantSnapshot() {
        IntegerWrapper wrapper = new IntegerWrapper(0);
        activityTestRule.getActivity().wrapDataSnapshotValue(nonExistant, wrapper);
        assertEquals(-1, wrapper.getInt());
    }

    @Test(expected = NullPointerException.class)
    public void wrapDataSnapshotValueWorksWithNullSnapshot() {
        IntegerWrapper wrapper = new IntegerWrapper(0);
        activityTestRule.getActivity().wrapDataSnapshotValue(null, wrapper);
        assertEquals(-1, wrapper.getInt());
    }

    @Test(expected = NullPointerException.class)
    public void wrapDataSnapshotValueWorksWithNullWrapper() {
        IntegerWrapper wrapper = new IntegerWrapper(0);
        activityTestRule.getActivity().wrapDataSnapshotValue(longIntegerValueSnapshot, null);
    }

    //tests for sufficientCurrency()

    @Test
    public void sufficientCurrencyWorks() {
        assertTrue(activityTestRule.getActivity().sufficientCurrency(1000, 500));
        assertTrue(activityTestRule.getActivity().sufficientCurrency(500, 500));
        assertTrue(activityTestRule.getActivity().sufficientCurrency(0, 0));
        assertFalse(activityTestRule.getActivity().sufficientCurrency(0, 500));
        assertFalse(activityTestRule.getActivity().sufficientCurrency(-500, -600));
    }

    //tests for updateUser()

    @Test
    public void updateUserWorks() {
        prepareLooperIfNotExisting();
        TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        activityTestRule.getActivity().updateUser(currentUser, testString, validNewStars, textView);
        assertEquals("Purchase successful.", textView.getText());
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWorksWithNullReference() {
        prepareLooperIfNotExisting();
        TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        activityTestRule.getActivity()
                .updateUser(null, testString, validNewStars, textView);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWorksWithNullString() {
        prepareLooperIfNotExisting();
        TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        activityTestRule.getActivity()
                .updateUser(currentUser, null, validNewStars, textView);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateUserWorksWithInvalidNewStars() {
        prepareLooperIfNotExisting();
        TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        activityTestRule.getActivity()
                .updateUser(currentUser, testString, invalidNewStars, textView);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWorksWithNullTextView() {
        prepareLooperIfNotExisting();
        activityTestRule.getActivity()
                .updateUser(currentUser, testString, validNewStars, null);
    }

    //tests for updateUserStars()

    @Test
    public void updateUserStarsWorks() {
        stars = invalidNewStars;
        activityTestRule.getActivity().updateUserStars(currentUserStars, validNewStars);

    }

    @Test(expected = IllegalArgumentException.class)
    public void updateUserStarsWorksWithNegativeNewStars() {
        activityTestRule.getActivity().updateUserStars(currentUserStars, invalidNewStars);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserStarsWorksWithNullReference() {
        activityTestRule.getActivity().updateUserStars(null, validNewStars);
    }

    //tests for addUserItem()

    @Test
    public void addUserItemWorks() {
        activityTestRule.getActivity().addUserItem(currentUserSpecificItem);
    }

    @Test(expected = NullPointerException.class)
    public void addUserItemWorksWithNullReference() {
        activityTestRule.getActivity().addUserItem(null);
    }

    //tests for setTextViewMessage()

    @Test
    public void setTextViewMessageWorks() {
        TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        activityTestRule.getActivity().setTextViewMessage(textView, testString);
        assertEquals(testString, textView.getText());
    }

    @Test(expected = NullPointerException.class)
    public void setTextViewMessageWorksWithNullTextView() {
        activityTestRule.getActivity().setTextViewMessage(null, testString);
    }

    @Test
    public void setTextViewMessageWorksWithNullString() {
        TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        activityTestRule.getActivity().setTextViewMessage(textView, null);
        assertEquals("", textView.getText());
    }

    //tests for resetTextViewMessage()

    @Test
    public void resetTextViewMessageWorks() {
        prepareLooperIfNotExisting();
        final TextView textView = new TextView(activityTestRule.getActivity()
                .getApplicationContext());
        textView.setText(testString);
        assertEquals(testString, textView.getText());
        activityTestRule.getActivity().resetTextViewMessage(textView, delay500);
        new CountDownTimer(delay500, delay500) {

            public void onTick(long millisUntilFinished) {
                /**
                 * Comment for the sake of CodeClimate.
                 */
            }

            public void onFinish() {
                assertEquals("", textView.getText());
            }
        }.start();
    }

    @Test
    public void resetTextViewMessageWorksWithZeroDelay() {
        final TextView textView = new TextView(activityTestRule.getActivity().getApplicationContext());
        textView.setText(testString);
        assertEquals(testString, textView.getText());
        activityTestRule.getActivity().resetTextViewMessage(textView, delayZero);
        new CountDownTimer(delayZero, delayZero) {

            public void onTick(long millisUntilFinished) {
                /**
                 * Comment for the sake of CodeClimate.
                 */
            }

            public void onFinish() {
                assertEquals("", textView.getText());
            }
        }.start();
    }

    @Test(expected = NullPointerException.class)
    public void resetTextViewMessageWorksWithNullTextView() {
        activityTestRule.getActivity().resetTextViewMessage(null, delay500);
    }

    //tests for gotoHome()

    @Test
    public void gotoHomeWorks() {
        Intents.init();
        activityTestRule.getActivity().gotoHome();
        intended(hasComponent(HomeActivity.class.getName()));
        Intents.release();
    }

    //tests for refreshShop()

    @Test
    public void refreshShopWorks() {
        Intents.init();
        activityTestRule.getActivity().refreshShop();
        intended(hasComponent(ShopTestActivity.class.getName()));
        Intents.release();
    }



    //retarded tests


    //helper methods
    private void prepareLooperIfNotExisting() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }
}
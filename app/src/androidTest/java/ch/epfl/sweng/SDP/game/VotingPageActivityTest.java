package ch.epfl.sweng.SDP.game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.firebase.Database;
import ch.epfl.sweng.SDP.home.HomeActivity;
import ch.epfl.sweng.SDP.utils.BitmapManipulator;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static ch.epfl.sweng.SDP.game.VotingPageActivity.disableAnimations;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class VotingPageActivityTest {

    private DataSnapshot dataSnapshotMock;
    private DatabaseError databaseErrorMock;
    private StarAnimationView starsAnimation;

    @Rule
    public final ActivityTestRule<VotingPageActivity> mActivityRule =
            new ActivityTestRule<VotingPageActivity>(VotingPageActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    VotingPageActivity.disableAnimations();
                }

                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra("RoomID", "0123457890");

                    return intent;
                }
            };

    @Before
    public void init() {
        dataSnapshotMock = Mockito.mock(DataSnapshot.class);
        databaseErrorMock = Mockito.mock(DatabaseError.class);
        starsAnimation = mActivityRule.getActivity()
                .findViewById(R.id.starsAnimation);
    }

    @Test
    public void ratingUsingRatingBarShouldBeSaved() {
        short counter = mActivityRule.getActivity().getChangeDrawingCounter();
        ((RatingBar) mActivityRule.getActivity().findViewById(R.id.ratingBar)).setRating(3);
        SystemClock.sleep(5000);
        assertThat(mActivityRule.getActivity().getRatings()[counter], is(3));
    }

    @Test
    public void addStarsHandlesBigNumber() {
        int previousStars = starsAnimation.getNumStars();
        starsAnimation.onSizeChanged(100, 100, 100, 100);
        Canvas canvas = new Canvas();
        starsAnimation.onDraw(canvas);
        starsAnimation.addStars(1000);
        starsAnimation.updateState(1000);
        starsAnimation.onDraw(canvas);
        assertThat(starsAnimation.getNumStars(), is(previousStars + 5));
    }

    @Test
    public void addStarsHandlesNegativeNumber() {
        int previousStars = starsAnimation.getNumStars();
        starsAnimation.onSizeChanged(100, 100, 100, 100);
        Canvas canvas = new Canvas();
        starsAnimation.onDraw(canvas);
        starsAnimation.addStars(-10);
        starsAnimation.updateState(1000);
        starsAnimation.onDraw(canvas);
        assertThat(starsAnimation.getNumStars(), is(previousStars));
    }

    @Test
    public void startHomeActivityStartsHomeActivity() {
        Intents.init();
        when(dataSnapshotMock.getValue(Integer.class)).thenReturn(4);
        mActivityRule.getActivity().callOnStateChange(dataSnapshotMock);
        SystemClock.sleep(2000);
        mActivityRule.getActivity().startHomeActivity(null);
        SystemClock.sleep(2000);
        intended(hasComponent(HomeActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testStateChange() {
        SystemClock.sleep(1000);
        when(dataSnapshotMock.getValue(Integer.class)).thenReturn(5);
        mActivityRule.getActivity().callOnStateChange(dataSnapshotMock);
        SystemClock.sleep(2000);

        RankingFragment myFragment = (RankingFragment) mActivityRule.getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.votingPageLayout);
        assertThat(myFragment.isVisible(), is(true));
    }

    @Test
    public void testShowDrawingImage() {
        Database.constructBuilder().addChildren("realRooms.0123457890.state").build().setValue(4);
        Bitmap image = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        image.eraseColor(android.graphics.Color.GREEN);
        mActivityRule.getActivity().callShowWinnerDrawing(image, "Champion");
    }

    @Test
    public void testChangeImage() {
        short counter = mActivityRule.getActivity().getChangeDrawingCounter();
        mActivityRule.getActivity().callChangeImage();
        SystemClock.sleep(6000);

        assertThat((int) mActivityRule.getActivity().getChangeDrawingCounter(),
                greaterThanOrEqualTo(counter + 1));
    }

    @Test(expected = DatabaseException.class)
    public void testOnCancelledListenerState() {
        when(databaseErrorMock.toException()).thenReturn(new DatabaseException("Cancelled"));
        mActivityRule.getActivity().listenerState.onCancelled(databaseErrorMock);
    }

    @Test(expected = DatabaseException.class)
    public void testOnCancelledListenerCounter() {
        when(databaseErrorMock.toException()).thenReturn(new DatabaseException("Cancelled"));
        mActivityRule.getActivity().listenerCounter.onCancelled(databaseErrorMock);
    }

    @Test
    public void testDecodeSampledBitmapFromResource() {
        Bitmap bitmap = BitmapManipulator.decodeSampledBitmapFromResource(
                mActivityRule.getActivity().getResources(), R.drawable.default_image, 2, 2);
        assertNotNull(bitmap);
    }

    @Test
    public void testDecodeSampledBitmapFromByteArray() {
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        int[] source = {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE};
        Bitmap bitmap = Bitmap.createBitmap(source, 2, 2, Bitmap.Config.ARGB_8888)
                .copy(Bitmap.Config.ARGB_8888, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG,
                1, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        Bitmap newBitmap = BitmapManipulator.decodeSampledBitmapFromByteArray(data, 0, data.length, 2, 2);
        assertNotNull(newBitmap);
    }
}

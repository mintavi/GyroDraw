package ch.epfl.sweng.GyroDraw;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import ch.epfl.sweng.GyroDraw.auth.LoginActivity;
import ch.epfl.sweng.GyroDraw.firebase.FbDatabase;
import ch.epfl.sweng.GyroDraw.firebase.OnSuccessValueEventListener;
import ch.epfl.sweng.GyroDraw.home.HomeActivity;
import ch.epfl.sweng.GyroDraw.utils.GlideUtils;
import ch.epfl.sweng.GyroDraw.utils.network.ConnectivityWrapper;

/**
 * Class representing the first page shown to the user upon first app launch.
 */
public class MainActivity extends NoBackPressActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_loading_screen);

        GlideUtils.startDotsWaitingAnimation(this);
        GlideUtils.startBackgroundAnimation(this);

        FirebaseApp.initializeApp(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null && ConnectivityWrapper.isOnline(this)) {
            FbDatabase.getUserByEmail(auth.getCurrentUser().getEmail(),
                    new OnSuccessValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            handleRedirection(dataSnapshot);
                        }
                    });
        } else {
            displayMainLayout();
        }
    }

    /**
     * Checks if account exists and redirects to {@link HomeActivity}.
     * Else shows the MainActivity.
     *
     * @param dataSnapshot the snapshot containing the response to evaluate
     */
    @VisibleForTesting
    public void handleRedirection(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            cloneAccountFromFirebase(dataSnapshot);

            TextView errorMessage = findViewById(
                    R.id.errorMessage);
            errorMessage.setTypeface(typeMuro);

            handleUserStatus(errorMessage);
        } else {
            displayMainLayout();
        }
    }

    private void displayMainLayout() {
        setContentView(R.layout.activity_main);
        GlideUtils.startBackgroundAnimation(this);

        findViewById(R.id.login_button).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ConnectivityWrapper.isOnline(getApplicationContext())) {
                            launchActivity(LoginActivity.class);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

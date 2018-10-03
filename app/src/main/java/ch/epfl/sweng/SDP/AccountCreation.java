package ch.epfl.sweng.SDP;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.TextView;

public class AccountCreation extends AppCompatActivity{
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance("https://gyrodraw.firebaseio.com/");
    private static final DatabaseReference databaseRef = database.getReference();
    private String userID = "b05";
    private TextInputLayout usernameInput;
    private Button createAcc;

    //testing stuff
    private TextView t1;
    private TextView t2;

    private View.OnClickListener createAccListener = new View.OnClickListener() {
      @Override
        public void onClick(View v) {
          createAccClicked();
      }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation);
        usernameInput = this.findViewById(R.id.usernameInput);
        createAcc = this.findViewById(R.id.createAcc);
        createAcc.setOnClickListener(createAccListener);

        t1 = this.findViewById(R.id.textView);
        t2 = this.findViewById(R.id.textView2);
    }

    private void createAccClicked() {
        t1.setText("clicked");
        t2.setText(databaseRef.toString());
        String username = usernameInput.getEditText().getText().toString();
        Account acc = new Account(username);
        databaseRef.child("users").child(userID).setValue(acc);
    }
}

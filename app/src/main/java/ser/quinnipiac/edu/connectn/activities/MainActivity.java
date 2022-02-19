package ser.quinnipiac.edu.connectn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import ser.quinnipiac.edu.connectn.R;
import ser.quinnipiac.edu.connectn.game_old_two.GameFactory;

/**
 * @author Thomas Kwashnak
 */
public class MainActivity extends AppCompatActivity {

    public static final String NAME;

    static {
        NAME = "name";
    }

    private EditText inputName;

    GameFactory gameFactory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputName = findViewById(R.id.main_input_name);

        /*
        These are less shaky than defining the method in the XML.
         */
        findViewById(R.id.main_button_play).setOnClickListener(view -> onPlay());
        findViewById(R.id.main_button_instructions).setOnClickListener(view -> onInstructions());
        findViewById(R.id.main_button_settings).setOnClickListener(view -> onSettings());

        if (savedInstanceState != null) {
            inputName.setText(savedInstanceState.getString(NAME));
            gameFactory = new GameFactory(savedInstanceState);
        } else {
            gameFactory = new GameFactory();
        }



    }

    public void onSettings() {
        Intent intent = new Intent(this,SettingsActivity.class);
        gameFactory.toBundle(intent.getExtras());
        startActivity(intent);
    }

    public void onPlay() {
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra(NAME, inputName.getText().toString());
        startActivity(intent);
    }

    public void onInstructions() {
        startActivity(new Intent(this, InstructionsActivity.class));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME, inputName.getText().toString());
    }
}
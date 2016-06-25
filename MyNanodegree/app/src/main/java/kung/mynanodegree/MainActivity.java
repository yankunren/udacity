package kung.mynanodegree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnMovies;
    private Button btnHawk;
    private Button btnBigger;
    private Button btnMaterial;
    private Button btnUbiquitous;
    private Button btnCapstone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    /**
     * init
     */
    private void initUI() {
        btnMovies = (Button) findViewById(R.id.btn_movies);
        btnHawk = (Button) findViewById(R.id.btn_hawk);
        btnBigger = (Button) findViewById(R.id.btn_bigger);
        btnMaterial = (Button) findViewById(R.id.btn_material);
        btnUbiquitous = (Button) findViewById(R.id.btn_ubiquitous);
        btnCapstone = (Button) findViewById(R.id.btn_capstone);

        btnMovies.setOnClickListener(this);
        btnHawk.setOnClickListener(this);
        btnBigger.setOnClickListener(this);
        btnMaterial.setOnClickListener(this);
        btnUbiquitous.setOnClickListener(this);
        btnCapstone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String msg = "";
        switch (v.getId()) {
            case R.id.btn_movies:
                msg = "movies";
                break;
            case R.id.btn_hawk:
                msg = "hawk";
                break;
            case R.id.btn_bigger:
                msg = "bigger";
                break;
            case R.id.btn_material:
                msg = "material";
                break;
            case R.id.btn_ubiquitous:
                msg = "ubiquitous";
                break;
            case R.id.btn_capstone:
                msg = "capstone";
                break;
        }
        Toast.makeText(this, getString(R.string.pop_message, msg), Toast.LENGTH_SHORT).show();
    }
}

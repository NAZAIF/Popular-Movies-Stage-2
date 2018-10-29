package app.nazaif.android.popularmoviesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import dev.nazaif.android.popularmoviesapp.R;

public class ReviewViewer extends AppCompatActivity {

    TextView tvContent, tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_viewer);
        tvAuthor = (TextView) findViewById(R.id.textviewAuthor);
        tvContent = (TextView) findViewById(R.id.textviewContent);

        String author = getIntent().getStringExtra("EXTRA_REVIEW_AUTHOR");
        String content = getIntent().getStringExtra("EXTRA_REVIEW_CONTENT");

        tvAuthor.setText(author);
        tvContent.setText(content);

    }

    public void back(View view) {
        finish();
    }

}

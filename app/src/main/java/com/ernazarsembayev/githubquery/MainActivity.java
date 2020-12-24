package com.ernazarsembayev.githubquery;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ernazarsembayev.githubquery.model.ProjectData;
import com.ernazarsembayev.githubquery.utilities.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    // Полученный результат в формате Json
    private  String searchResultJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    private void displaySearchResult() {
        ProjectData projectData = new Gson().fromJson(searchResultJson, ProjectData.class);
        mSearchResultsTextView.setText(String.valueOf(projectData));
    }


    /**
     * Метод извлекает поисковый текст из EditText,
     * создает URL-адрес (используя {NetworkUtils})
     * для репозитория github, отображает этот URL-адрес в TextView и,
     * запрашивает AsyncTaskLoader выполнить запрос GET.
     */
    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        new GithubQueryTask().execute(githubSearchUrl);
    }

    /**
     * Метод показывает результат
     * и скрывает сообщение об ошибке
     */
    private void showJsonDataView() {
        // Сначала скрыть сообщение об ошибке
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Вывести результаты
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Этот метод показывает сообщение об ошибке
     * и скрывает результат
     */
    private void showErrorMessage() {
        // Скрыть TextView с результатами
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        // Показать сообщение об ошибке
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // Класс для выполнения запроса в новом потоке
    @SuppressLint("StaticFieldLeak")
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // При загрузке показать индикатор загрузки
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        // Запрос GitHub
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {

            // После загрузки скрыть индикатор
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                // Вызвать showJsonDataView если результат валидный, non-null
                showJsonDataView();
                // Сохранить загруженые данные в searchResultJson
                searchResultJson = githubSearchResults;
                displaySearchResult();
            } else {
                // Вызвать showErrorMessage если результат Null
                showErrorMessage();
            }
        }
    }

    // Кнопка поиск в меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // При нажатии на поиск
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
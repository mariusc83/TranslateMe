package org.mariusconstantin.translateme.main.translate;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mariusconstantin.translateme.R;
import org.mariusconstantin.translateme.main.MainActivity;
import org.mariusconstantin.translateme.repositories.misc.CountryModel;
import org.mariusconstantin.translateme.main.inject.DaggerTranslateComponent;
import org.mariusconstantin.translateme.main.inject.TranslateComponent;
import org.mariusconstantin.translateme.main.inject.TranslateModule;

import java.util.Collections;
import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


/**
 * Created by MConstantin on 7/26/2016.
 */
public class TranslateFragment extends Fragment implements TranslateContract.ITranslateView {

    private TextView mResultsField;
    private EditText mInputField;
    private Button mLaunchTranslateButton;
    private TranslateContract.ITranslatePresenter mPresenter;
    private TranslateComponent mTranslateComponent;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTranslateComponent = DaggerTranslateComponent
                .builder()
                .mainComponent(((MainActivity) (getActivity())).getMainComponent())
                .translateModule(new TranslateModule(this))
                .build();
        mPresenter = mTranslateComponent.getTranslatePresenter();
        checkNotNull(mPresenter, "The presenter cannot be null");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.translate_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultsField = (TextView) view.findViewById(R.id.translated_text_field);
        mInputField = (EditText) view.findViewById(R.id.input_field);
        mLaunchTranslateButton = (Button) view.findViewById(R.id.translate_button);
        mLaunchTranslateButton.setEnabled(false);
        mInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setLaunchButtonStatus(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLaunchTranslateButton
                .setOnClickListener(target ->
                        mPresenter.translate(mInputField.getText().toString()));

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onStop();
    }

    private void setLaunchButtonStatus(@Nullable CharSequence inputText) {
        mLaunchTranslateButton.setEnabled(!TextUtils.isEmpty(inputText));
    }

    @Override
    public void displayMessage(@NonNull String s) {
        mResultsField.setText(s);
    }

    @Override
    public void displayMessage(@StringRes int messageId) {
        mResultsField.setText(getString(messageId));
    }

    @Override
    public void setPresenter(@NonNull TranslateContract.ITranslatePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isVisible();
    }


    public static class SpinnerAdapter extends BaseAdapter implements ThemedSpinnerAdapter {
        @NonNull
        private final ThemedSpinnerAdapter.Helper mHelper;
        @NonNull
        private final List<CountryModel> mData;

        public SpinnerAdapter(Context context, @NonNull List<CountryModel> data) {
            mHelper = new ThemedSpinnerAdapter.Helper(context);
            mData = data;
        }

        public SpinnerAdapter(Context context) {
            mHelper = new ThemedSpinnerAdapter.Helper(context);
            mData = Collections.emptyList();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder viewHolder;
            if (view == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item,
                        parent,
                        false);
                viewHolder = new ViewHolder((TextView) view.findViewById(android.R.id.text1));
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.getTextView().setText(mData.get(position).toString());
            return view;
        }

        @Override
        public void setDropDownViewTheme(@Nullable Resources.Theme theme) {
            mHelper.setDropDownViewTheme(theme);
        }

        @Nullable
        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mHelper.getDropDownViewTheme();
        }

        private static class ViewHolder {
            @NonNull
            public final TextView mTextView;

            private ViewHolder(@NonNull TextView textView) {
                mTextView = textView;
            }

            @NonNull
            public TextView getTextView() {
                return mTextView;
            }
        }
    }
}

package com.jia16.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jia16.R;
import com.jia16.util.CommoUtil;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ShowPwdEditText extends LinearLayout {

    private EditText editTextPwd;
    private Context context;

    public ShowPwdEditText(Context context) {
        this(context, null);
    }

    public ShowPwdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowPwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        View content = View.inflate(context, R.layout.edit_text_show_pwd, this);
        editTextPwd = (EditText) content.findViewById(R.id.edittext_password);
        final CheckBox checkBox = (CheckBox) content.findViewById(R.id.rb_pwd_show_hint);

        content.findViewById(R.id.ll_pwd_show_hint).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                checkBox.setChecked(!checkBox.isChecked());
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    //显示密码
                    editTextPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {

                    //隐藏密码
                    editTextPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                editTextPwd.setSelection(editTextPwd.getText().length());
            }
        });

        editTextPwd.setFocusable(true);
        editTextPwd.setFocusableInTouchMode(true);
        editTextPwd.requestFocus();
        CommoUtil.openKey(context, editTextPwd); //打开键盘
    }

    public String getText(){

        return editTextPwd.getText().toString();
    }

    public void setText(String str){

        editTextPwd.setText(str);
    }

    public void closeKeyBoard(){

        InputMethodManager imm = ( InputMethodManager ) this.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( this.getApplicationWindowToken( ) , 0 );

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        editTextPwd.setFocusable(true);
        return true;
    }


}

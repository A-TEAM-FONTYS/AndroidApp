package com.example.appusagedata.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.appusagedata.MainActivity
import com.example.appusagedata.R
import com.example.appusagedata.viewmodels.SignInViewModel
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onStart() {
        sign_in_btn.setOnClickListener {
            (activity as MainActivity?)?.login(username_text_edit.text.toString(), password_text_edit.text.toString())
        }

        sign_in_text.setOnClickListener{
            (activity as MainActivity?)?.loadFragment(SignUpFragment())
        }
        super.onStart()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)

    }

}

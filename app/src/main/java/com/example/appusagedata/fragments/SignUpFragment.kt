package com.example.appusagedata.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appusagedata.MainActivity

import com.example.appusagedata.R
import com.example.appusagedata.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onStart(){
        sign_up_btn.setOnClickListener {
            (activity as MainActivity?)?.loadFragment(DataFragment())
        }

        sign_up_text.setOnClickListener{
            (activity as MainActivity?)?.loadFragment(SignInFragment())
        }

        super.onStart()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

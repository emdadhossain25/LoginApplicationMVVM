package com.emdad.loginapplicationmvvm.ui.login

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.emdad.loginapplicationmvvm.R
import kotlinx.android.synthetic.main.main_fragment.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login.setOnClickListener {
            viewModel.onSubmit(
                email = email.text.toString(),
                password = password.text.toString()
            )
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                LoginViewState.Idle -> {
                    email.isEnabled = true
                    password.isEnabled = true
                    btn_login.isEnabled = true
                    progress.isGone = true
                    password.error = null
                    email.error = null
                }
                LoginViewState.Progress -> {
                    email.isEnabled = false
                    password.isEnabled = false
                    btn_login.isEnabled = false
                    progress.isVisible = true
                }
                is LoginViewState.Failed -> {
                    email.isEnabled = true
                    password.isEnabled = true
                    btn_login.isEnabled = true
                    progress.isGone = true
                    if (result.message.contains("Email")) {
                        password.error = null
                        email.error = result.message
                    } else if (result.message.contains("Password")) {
                        password.error = result.message
                        email.error = null
                    } else {
                        password.error = null
                        email.error = null
                        link_signup.text = result.message
                    }
                }
                is LoginViewState.Succeed -> {
                    email.isEnabled = true
                    password.isEnabled = true
                    btn_login.isEnabled = true
                    progress.isGone = true
                    password.error = null
                    email.error = null
                    link_signup.text = result.message
                }
            }
        })
    }

}


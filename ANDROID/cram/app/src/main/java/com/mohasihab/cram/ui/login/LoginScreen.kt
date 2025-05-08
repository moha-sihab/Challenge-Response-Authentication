package com.mohasihab.cram.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse
import com.mohasihab.cram.core.helper.BiometricHelper
import com.mohasihab.cram.core.helper.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    snackBarHostState: SnackbarHostState,
    onLoginSuccess: (username: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity

    val scope = rememberCoroutineScope()

    val loginState by viewModel.loginState.collectAsState()
    var isLoading = false
    var errorMessage : String? = null
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable  { mutableStateOf("") }
    var passwordVisible by rememberSaveable  { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.checkLoginStatus()
    }

    LaunchedEffect(loginState.uiState) {
        when(loginState.uiState){
            is UiState.Success -> {
                isLoading = false
                val username = (loginState.uiState as UiState.Success<BaseResponse<LoginResponse>>).data.data?.username ?: ""
                scope.launch {
                    snackBarHostState.showSnackbar("Hi $username, Login Success")
                }
                delay(200)
                onLoginSuccess(username)
            }
            is UiState.Error-> {
                isLoading = false
                errorMessage = (loginState.uiState as UiState.Error).message
                scope.launch {
                    snackBarHostState.showSnackbar("Error: $errorMessage")
                }
                viewModel.resetState()
            }
            is UiState.Loading -> {
                isLoading = true
            }
            else -> {
                isLoading = false
                errorMessage = null
            }
        }

    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CRAM Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                viewModel.login(username,password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                activity.let {
                    BiometricHelper.authenticate(
                        activity = it,
                        onSuccess = {

                                viewModel.onBiometricSuccess()

                        },
                        onError = { error -> Log.e("Biometric", error) }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Fingerprint,
                contentDescription = "Biometric Icon",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Login with Biometrics")
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, color = Color.Red)
        }
    }
}
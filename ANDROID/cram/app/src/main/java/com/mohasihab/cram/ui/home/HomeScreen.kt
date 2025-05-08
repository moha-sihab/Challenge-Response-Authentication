package com.mohasihab.cram.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.mohasihab.cram.core.helper.BiometricHelper
import com.mohasihab.cram.core.helper.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    snackBarHostState: SnackbarHostState,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    val state = viewModel.homeState.collectAsState()
    val publicKey = state.value.successPublicKey
    var username = ""
    when(state.value.uiState){
        is UiState.Success -> {
            username = (state.value.uiState as UiState.Success).data
        }
        else -> {
        }
    }

    LaunchedEffect(state.value.successPublicKey) {
        if(publicKey){
            scope.launch {
                snackBarHostState.showSnackbar("Generate Public Key Success")
            }
            delay(200)
        }

    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, $username!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "You have logged in using CRAM simulation.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    activity.let {
                        BiometricHelper.authenticate(
                            activity = it,
                            onSuccess = {
                                BiometricHelper.deleteKey()
                                val publicKey = BiometricHelper.generateRSAKeyPair()
                                publicKey.let { pub ->
                                    val encoded = BiometricHelper.encodePublicKey(pub)
                                    viewModel.sendPublicKey(encoded)
                                }
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
                Text("Authenticate with Biometrics")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick ={
                    viewModel.logout()
                    onLogoutClick()
                } ,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}
package com.levi.qxdapp.presentation.client.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.levi.qxdapp.data.local.UserProfileManager

private val BluePrimary = Color(0xFF1964C3)
private val BlueDark = Color(0xFF0D47A1)
private val GreenAccent = Color(0xFF4CAF50)
private val BackgroundGray = Color(0xFFF5F6FA)
private val LightBlueBackground = Color(0xFFE3EDFB)
private val OrangeSupplier = Color(0xFFFF6F00)
private val LightOrangeBackground = Color(0xFFFFF3E0)
private val RedLogout = Color(0xFFD32F2F)
private val RedLogoutBackground = Color(0xFFFDE8E8)
private val DividerColor = Color(0xFFEEEEEE)
private val GrayLabel = Color(0xFF9E9E9E)

@Composable
fun ClientProfileScreen(onLogoutClick: () -> Unit = {}) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf(UserProfileManager.getFirstName(context)) }
    var lastName by remember { mutableStateOf(UserProfileManager.getLastName(context)) }
    var phone by remember { mutableStateOf(UserProfileManager.getPhone(context)) }
    var photoPath by remember { mutableStateOf(UserProfileManager.getPhotoPath(context)) }
    var addresses by remember { mutableStateOf(UserProfileManager.getAddresses(context)) }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddressesDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = UserProfileManager.saveImageToInternalStorage(context, it)
            if (savedPath != null) {
                UserProfileManager.setPhotoPath(context, savedPath)
                photoPath = savedPath
                Toast.makeText(context, "Foto de perfil atualizada!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (showEditProfileDialog) {
        var tempFirstName by remember { mutableStateOf(firstName) }
        var tempLastName by remember { mutableStateOf(lastName) }
        var tempPhone by remember { mutableStateOf(phone) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = "Editar Perfil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BluePrimary
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = tempFirstName,
                        onValueChange = { tempFirstName = it },
                        label = { Text("Nome") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempLastName,
                        onValueChange = { tempLastName = it },
                        label = { Text("Sobrenome") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempPhone,
                        onValueChange = { tempPhone = it },
                        label = { Text("Telefone") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempFirstName.isBlank()) {
                            Toast.makeText(context, "O nome não pode estar em branco", Toast.LENGTH_SHORT).show()
                        } else {
                            firstName = tempFirstName.trim()
                            lastName = tempLastName.trim()
                            phone = tempPhone.trim()
                            
                            UserProfileManager.setFirstName(context, firstName)
                            UserProfileManager.setLastName(context, lastName)
                            UserProfileManager.setPhone(context, phone)
                            
                            showEditProfileDialog = false
                            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Salvar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditProfileDialog = false }
                ) {
                    Text("Cancelar", color = GrayLabel)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    if (showAddressesDialog) {
        var newAddressText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddressesDialog = false },
            title = {
                Text(
                    text = "Meus Endereços",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = BluePrimary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = newAddressText,
                            onValueChange = { newAddressText = it },
                            placeholder = { Text("Novo endereço...") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (newAddressText.isNotBlank()) {
                                    UserProfileManager.addAddress(context, newAddressText.trim())
                                    addresses = UserProfileManager.getAddresses(context)
                                    newAddressText = ""
                                    Toast.makeText(context, "Endereço adicionado!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text("Adicionar", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (addresses.isEmpty()) {
                            Text(
                                text = "Nenhum endereço cadastrado.",
                                color = GrayLabel,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            )
                        } else {
                            addresses.toList().forEach { address ->
                                val addressValue = address
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = BackgroundGray),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 18.dp)
                                    ) {
                                        Text(
                                            text = addressValue,
                                            fontSize = 15.sp,
                                            color = Color(0xFF333333),
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = {
                                                val currentList = UserProfileManager.getAddresses(context).toMutableList()
                                                val removeIndex = currentList.indexOf(addressValue)
                                                if (removeIndex >= 0) {
                                                    currentList.removeAt(removeIndex)
                                                    UserProfileManager.setAddresses(context, currentList)
                                                    addresses = UserProfileManager.getAddresses(context)
                                                    Toast.makeText(context, "Endereço removido!", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Remover endereço",
                                                tint = RedLogout,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAddressesDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Fechar", color = Color.White)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSection(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                photoPath = photoPath,
                onEditPhotoClick = { photoPickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(0f)
            )

            StatsCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 40.dp)
                    .zIndex(1f)
            )
        }

        Spacer(modifier = Modifier.height(56.dp))

        MenuCard(
            onAddressesClick = { showAddressesDialog = true },
            onEditProfileClick = { showEditProfileDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LogoutButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun HeaderSection(
    firstName: String,
    lastName: String,
    phone: String,
    photoPath: String?,
    onEditPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initials = "${firstName.firstOrNull() ?: ""}${lastName.firstOrNull() ?: ""}"
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(BluePrimary)
            .padding(top = 16.dp, bottom = 60.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Perfil",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AvatarWithBadge(
                photoPath = photoPath,
                initials = initials,
                onEditPhotoClick = onEditPhotoClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (lastName.isNotEmpty()) "$firstName $lastName" else firstName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = phone,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AvatarWithBadge(
    photoPath: String?,
    initials: String,
    onEditPhotoClick: () -> Unit
) {
    val bitmap = remember(photoPath) {
        if (photoPath != null) {
            try {
                android.graphics.BitmapFactory.decodeFile(photoPath)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(BlueDark)
                .clickable { onEditPhotoClick() },
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Text(
                    text = initials.uppercase(),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .size(26.dp)
                .offset(x = (-2).dp, y = (-2).dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(GreenAccent)
                .clickable { onEditPhotoClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar foto",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun StatsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(value = "12", label = "Pedidos")
            VerticalDivider()
            StatItem(value = "3", label = "Fornecedores")
            VerticalDivider()
            StatItem(value = "4.9", label = "Avaliação")
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = BluePrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = GrayLabel,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(DividerColor)
    )
}

@Composable
private fun MenuCard(
    onAddressesClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            MenuRow(
                icon = Icons.Filled.Edit,
                label = "Editar Dados Pessoais",
                onClick = onEditProfileClick
            )
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            MenuRow(
                icon = Icons.Filled.LocationOn,
                label = "Meus Endereços",
                onClick = onAddressesClick
            )
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            MenuRow(
                icon = Icons.Filled.CreditCard,
                label = "Formas de Pagamento",
                onClick = {}
            )
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            MenuRow(
                icon = Icons.Filled.Notifications,
                label = "Notificações",
                onClick = {}
            )
            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            MenuRow(
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                label = "Ajuda e Suporte",
                onClick = {}
            )
        }
    }
}

@Composable
private fun MenuRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightBlueBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = BluePrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
private fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = RedLogoutBackground
        ),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFF5C6C6))
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            tint = RedLogout,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Sair da conta",
            color = RedLogout,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ClientProfileScreenPreview() {
    ClientProfileScreen()
}

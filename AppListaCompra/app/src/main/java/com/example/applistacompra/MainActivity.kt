package com.example.applistacompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                    ListaCompra()

            }
        }
    }

@Composable
fun ListaCompra() {
    //Variables
    val itemList = remember { mutableStateListOf<Pair<String, Color>>() } //Nombre, color
    var showDialog by remember { mutableStateOf(false) } //Cuadro dialogo (true o false)
    var itemName by remember { mutableStateOf("") } //variable para almacenar nombres
    var itemColor by remember { mutableStateOf(Color.Transparent) } //Color inicial para los iconos (transparente)
    var itemToDelete: Pair<String, Color>? by remember { mutableStateOf(null) } //Almacena elemento a eliminar
    var confirmDelete by remember { mutableStateOf(false) } //Variable para confirmar el delete

    Scaffold(
        // ---> boton flotante (+Elemento) <---
        floatingActionButton = {
            Column(
                //Alineaccion horizontal y vertical de la columna con espacio de 8dp
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Contador flotante
                FloatingActionButton(
                    onClick = {}, containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(itemList.size.toString()) //Mostrar numero
                }

                //Si hace clic = ShowDialog es true (entra al if mas abajo)
                FloatingActionButton(onClick = { showDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir Elemento")
                }
            }
        }
    )

    { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (itemList.isEmpty()) {
                Text(text = "No hay elementos en la lista.", modifier = Modifier.padding(16.dp))
            }else {
                //--> LISTA <--
                LazyColumn {
                    //Funcion "items" itera sobre cada elemento de itemList (lista item)
                    items(itemList) { item ->
                        val (name, color) = item

                        Row(
                            //Elementos vertical centrado y con padding entre ellos
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            //Icono ShoppingCart
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = color)
                            //Espacio entre texto - icono Carrito
                            Spacer(Modifier.width(8.dp))
                            //Muestra nombre y fecha en la lita de elementos
                            Text(text = name )

                            //--> BORRAR ELEMENTO <---
                            //Boton eliminar elemento
                            IconButton(onClick = {
                                itemToDelete = item //Guarda el elemento al eliminar
                                confirmDelete = true //Muestra la confirmación
                            }) {

                                //Icono Delete
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Item")
                            }
                        }
                    }
                }
            }
            //Si confirmDelete y itemToDelete es diferente a null entra al if...
            if (confirmDelete && itemToDelete != null) {
                //--> CUADRO DE DIALOGO <--
                AlertDialog(
                    //Pulsa fuera > oculta cuadro dialogo
                    onDismissRequest = { confirmDelete = false },
                    title = { Text("Confirmar eliminación") },
                    //${itemToDelete!!.first} = nombre del item a eliminar
                    text = { Text("¿Estás seguro de que deseas eliminar '${itemToDelete!!.first}'?") },

                    //Confirmar eliminar elemento
                    confirmButton = {
                        TextButton(onClick = {
                            itemList.remove(itemToDelete) //Eliminar elemento seleccionado
                            confirmDelete = false
                            itemToDelete = null
                        }) {
                            Text("Confirmar")
                        }
                    },
                    //Cancelar confirmar elemento
                    dismissButton = {
                        TextButton(onClick = {
                            confirmDelete = false
                            itemToDelete = null
                        }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
            //Si showDialog = true ... Añade elemento a lista
            if (showDialog) {

                //--> CUADRO DE DIALOGO <--
                AlertDialog(
                    //Pulsa fuera > oculta cuadro dialogo
                    onDismissRequest = { showDialog = false },
                    title = { Text("Añadir elemento") },

                    //Contenido del cuadro Dialogo
                    text = {
                        Column {
                            //Texto para nombre del elemento
                            TextField(
                                value = itemName,
                                //Cuando escribe actualiza el nombre del elemento
                                onValueChange = { itemName = it },
                                label = { Text("Introduce el nombre") }
                            )


                            Spacer(Modifier.height(16.dp))

                            Text("Select a Color:")

                            Row(
                                //Elementos horizontal centrado y con padding entre ellos
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                // Lista de colores
                                val colors = listOf(
                                    Color.Red,
                                    Color.Green,
                                    Color.Blue,
                                    Color.Yellow,
                                    Color.Cyan
                                )

                                //bucle forEech de color creando un box(caja) para cada color
                                colors.forEach { color ->
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(color)
                                            .clickable { itemColor = color }
                                    )
                                }
                            }
                        }
                    },

                    confirmButton = {
                        TextButton(onClick = {
                            //Si item no es vacio y color no es transparente..
                            if (itemName.isNotEmpty() && itemColor != Color.Transparent) {
                                //Añade item a la lista con nombre y color
                                itemList.add(itemName to itemColor)
                                itemName = ""
                                itemColor = Color.Transparent
                                showDialog = false
                            }
                        }) { Text("añadir") }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMessageCard() {
    ListaCompra()
}
package com.example.synapseapp.ui.theme.fargments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.synapseapp.airequests.AI
import com.example.synapseapp.airequests.api.API


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.String
import kotlin.math.pow
import kotlin.math.round


@Serializable
data class PricingUnits(
    val prompt: String? = null,
    val completion: String? = null,
    val image_output: String? = null
)

@Serializable
data class Architecture(
    val input_modalities: List<String>,
    val output_modalities: List<String>
)

@Serializable
data class Pricing(
    val prompt: Double? = null,
    val completion: Double? = null
)

@Serializable
data class InfoOfAI(
    val id: String,
    val name: String,
    val pricing: Pricing,
    val pricing_units: PricingUnits,
    val architecture: Architecture
)

@Serializable
data class ResponseWrapper(
    val data: List<InfoOfAI>
)

@Serializable
data class Credits(
    val credits: Double = 0.0
)

@Serializable
data class ResponseCredits(
    val data: Credits
)

data class AiModel(
    val provider: String,
    val name: String,
    val promptPrice: Double,
    val completionPrice: Double,
    val id: String,
    var isSelected: Boolean = false
)

var popularAIModelsList: List<AiModel> = mutableListOf()
var otherAIModelsList: List<AiModel> = mutableListOf()


val mainAICompanies = listOf("google/gemini", "anthropic", "openai/gpt", "deepseek")

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
        })
    }
}

fun Double.roundDecimal(digits: Int): Double {
    val factor = 10.0.pow(digits.toDouble())
    return round(this * factor) / factor
}

suspend fun List<InfoOfAI>.structureModesInfo(): List<AiModel> = map { model ->
    val promptPrice = if (model.pricing_units.prompt == "token")
        (model.pricing.prompt ?: 0.0) * 1_000_000
    else 0.0

    val completionPrice = if (model.pricing_units.completion == "token")
        (model.pricing.completion ?: 0.0) * 1_000_000
    else 0.0

    AiModel(
        provider = model.name.substringBefore(": "),
        name = model.name.substringAfter(": "),
        promptPrice = promptPrice.roundDecimal(2),
        completionPrice = completionPrice.roundDecimal(2),
        id = model.id
    )
}

val VERSION_REGEX = Regex("""(\d+(?:\.\d+)?)""")

fun versionOf(name: String): Double =
    VERSION_REGEX.find(name)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0

suspend fun shapeIntoModels(
    responseList: MutableList<InfoOfAI>?
) {
    if (responseList == null) return
    responseList.removeIf { it.id.contains('~') }

    popularAIModelsList = mainAICompanies
        .flatMap { company ->
            responseList.filter { article ->
                article.id.contains(company, ignoreCase = true) &&
                        article.architecture.output_modalities == listOf("text")
            }
                .takeLast(10)
        }
        .structureModesInfo()
        .groupBy { it.provider }
        .flatMap { (_, group) ->
            group.sortedByDescending { versionOf(it.name) }
        }

    otherAIModelsList = responseList.filter { article ->
        mainAICompanies.none { word -> article.id.contains(word, ignoreCase = true) }
    }.structureModesInfo()
}

suspend fun fetchCredits(): Double? {
    try {
        val response: ResponseCredits = client.get("https://routerai.ru/api/v1/credits") {
            header("Authorization", "Bearer ${API.API_KEY}")
        }.body()
        return response.data.credits.roundDecimal(2)
    } catch (e: Exception) {
        println(e.message)
    }
    return null
}


suspend fun fetchAIModelsCatalog(): MutableList<InfoOfAI>? {
    try {
        val response: ResponseWrapper = client.get("https://routerai.ru/api/v1/models").body()
        val responseList: MutableList<InfoOfAI> = response.data as MutableList<InfoOfAI>
        return responseList
    } catch (e: Exception) {
        println(e.message)
    }
    return null
}


@Composable
fun AIModelPicker(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        val responseList: MutableList<InfoOfAI>? = fetchAIModelsCatalog()
        //println("баланс: ${fetchCredits()}")
        shapeIntoModels(responseList)
        client.close()
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(x = (-10).dp, y = 15.dp),
        shape = RoundedCornerShape(25.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .width(300.dp)
            .heightIn(max = 500.dp)
    ) {
        // Log.v("popularAIModelsList", popularAIModelsList.toString())
        var showOtherModels by remember { mutableStateOf(false) }
        if (!showOtherModels) GroupOfModel(
            onItemClick = onItemClick,
            onDismiss = onDismiss,
            popularAIModelsList
        )
        else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(28.dp))
                    .clickable { showOtherModels = false }
                    .padding(horizontal = 16.dp,vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = "Вернуться назад",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Назад",
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                thickness = 1.dp
            )
            GroupOfModel(onItemClick = onItemClick, onDismiss = onDismiss, otherAIModelsList)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(28.dp))
                .clickable { showOtherModels = true }
                .padding(horizontal = 25.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Все модели",
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Открыть список моделей",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

    }
}

@Composable
fun GroupOfModel(
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit,
    AIModelsList: List<AiModel>
) {
    var aiModelProvider: String = ""

    AIModelsList.forEach { item ->
        if (aiModelProvider != item.provider) {
            aiModelProvider = item.provider
            Text(
                text = aiModelProvider,
                modifier = Modifier.padding(start = 25.dp),
                fontSize = 25.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                thickness = 1.dp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(28.dp))
                .background(
                    color =
                        if (item.isSelected) MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.10f)
                        else Color.Transparent
                )
                .combinedClickable(
                    onClick = {
                        // Log.v("AIModelPicker", "Click: ${item.id}")
                        AI.currentAIModelForRequest = item.id
                        // Log.v("currentAIModelForRequest", AI.currentAIModelForRequest)
                        onItemClick(item.id)
                        onDismiss()
                    },
                    onLongClick = {
                        // Log.v("AIModelPicker", "Long Click: ${item.id}")
                    }
                )
                .padding(horizontal = 16.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Вход: ${item.promptPrice} ₽ за 1М токенов",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Выход: ${item.completionPrice} ₽ за 1М токенов",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
    }
//    HorizontalDivider(
//        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
//        thickness = 1.dp
//    )
    //Spacer(modifier = Modifier.height(4.dp))
}

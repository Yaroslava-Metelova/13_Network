
import jdk.nashorn.internal.codegen.CompilerConstants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.logging.Logger
import javax.security.auth.callback.Callback
import javax.xml.ws.Response


fun main(args: Array<String>) {
    val baseUrl = "https://official-joke-api.appspot.com/jokes/ten/"
    var catFactsDataCollection = requestDataFrom(baseUrl)

    val log = Logger.getLogger(catFactsDataCollection?.javaClass?.name)
    log.info(catFactsDataCollection.toString())

}

fun requestDataFrom(baseUrl: String): Joke? {
    var dataReturn: ArrayList<Joke>? = null

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(Service::class.java)
    val call = service.getCatFactData()

    call.enqueue( object : Callback<Joke!>{
        override fun onResponse(call: CompilerConstants.Call, response: Response<Joke>) {
            if (response.isSuccessful) {
                dataReturn = response.body()
            }
        }

        override fun onFailure(call: CompilerConstants.Call, t: Throwable) {
            println(t.message)
        }
    })

    while (dataReturn == null) {
        println("uploading data...")
        Thread.sleep(300)
    }

    return dataReturn
}


interface Service {
    @GET
    fun getCatFactData(): Call<Joke>
}
data class Joke (

    @SerializedName("id") var id : Int,
    @SerializedName("type") var type : String,
    @SerializedName("setup") var setup : String,
    @SerializedName("punchline") var punchline : String

)

data class CatFactResponseItem(
    @SerializedName("__v")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deleted")
    val deleted: Boolean,
    @SerializedName("source")
    val source: String,
    @SerializedName("status")
    val status: Status,
    @SerializedName("text")
    val text: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("used")
    val used: Boolean,
    @SerializedName("user")
    val user: String
) {
    override fun toString(): String {
        return "\n" +
                "Status: " +
                status +
                "\n" +
                "Text: " +
                text +
                "\n" +
                "Updated by User \"$user\" at: $updatedAt" +
                "\n"
    }
}

data class Status(
    @SerializedName("sentCount")
    val sentCount: Int,
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("feedback")
    val feedback: String
) {
    override fun toString(): String {
        if (verified) return "verified"
        return "non-verified"
    }
}
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codelabs.gameservices.huawei.R
import com.huawei.hms.jos.games.achievement.Achievement


//AchievementListAdapter is for Recycler View to show Achievements

class AchievementListAdapter(private val context: Context) :
    RecyclerView.Adapter<AchievementListAdapter.ModelViewHolder>() {

    private var achievementList = listOf<Achievement>()

    fun setAchievementList(list: List<Achievement>) {
        achievementList = list
        notifyDataSetChanged()
    }

    class ModelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //We have initialize our TextViews here.
        val achievementName: TextView = view.findViewById(R.id.achievement_name)
        val achievementDescription: TextView = view.findViewById(R.id.achievement_des)
        val achievementImage: ImageView = view.findViewById(R.id.achievement_image)
        val achievementState: TextView = view.findViewById(R.id.achievement_state)
        val achievementSteps: TextView = view.findViewById(R.id.achievement_increment)


        //We have set our single Achievement Item to UI objects. We will use it in onBindViewHolder function
        @SuppressLint("SetTextI18n")
        fun bindItems(item: Achievement, context: Context) {

            //If Achievement is not hidden
            if (item.state != 2) {
                achievementName.text = item.displayName
                achievementDescription.text = item.descInfo

                //If Achievement has steps to unlock
                if (item.type == 2) {
                    achievementSteps.visibility = View.VISIBLE
                    achievementSteps.text = "Steps:${item.reachedSteps}/${item.allSteps}"
                } else
                    achievementSteps.visibility = View.GONE
                //Download Image from the Achievement URI
                Glide.with(context).load(item.reachedThumbnailUri).into(achievementImage)
            }
            //Display Achievement Information according to its state ( Unlocked, Hidden, Revealed )
            when (item.state) {
                1 -> achievementState.text = "Revealed"
                2 -> {
                    achievementState.text = "Hidden"
                    achievementName.text = "Top Secret"
                    achievementDescription.text = "Progress through the game play to unlock this"
                    achievementSteps.visibility = View.GONE
                }
                3 -> achievementState.text = "Unlocked"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        //Inflate layout according to our resource file list_view_item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_view_item, parent, false)
        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return achievementList.size
    }

    //This is the essential function for binding all the information to the items
    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bindItems(achievementList[position], context)
    }
}
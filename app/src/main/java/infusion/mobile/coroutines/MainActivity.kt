package infusion.mobile.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.data_item.view.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: DataViewModel
    private val dataAdapter = DataAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        dataRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = dataAdapter
        }

        startButton.setOnClickListener {

            viewModel.getData()

            observeViewModel()
        }
    }

    private fun observeViewModel() {

        viewModel.progress.observe(this, { isLoading ->
            isLoading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    dataRecyclerView.visibility = View.GONE
                }
            }
        })

        viewModel.datas.observe(this, { data ->
            data?.let {
                dataRecyclerView.visibility = View.VISIBLE
                progressBar.visibility =View.GONE
                dataAdapter.updateData(it)
            }
        })
    }

    class DataAdapter(var datas: ArrayList<Data>): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

        fun updateData(users: List<Data>) {
            datas.clear()
            datas.addAll(users)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        )

        override fun getItemCount() = datas.size

        override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
            holder.bind(datas[position])
        }

        class DataViewHolder(view: View): RecyclerView.ViewHolder(view) {

            private val imageView = view.userImg
            private val firstName = view.firstName
            private val lastName = view.lastName
            private val email = view.mailId

            fun bind(data: Data) {
                firstName.text = data.first_name
                lastName.text=data.last_name
                email.text = data.email

                Picasso.get().load(data.avatar).into(imageView)
            }
        }
    }
}
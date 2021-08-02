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

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: DataViewModel
    private val dataAdapter = DataAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        viewModel.loadData()

        dataRecyclerView.apply {
            layoutManager = LinearLayoutManager(this)
            adapter = dataAdapter
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.datas.observe(this, Observer { countries ->
            countries?.let {
                dataRecyclerView.visibility = View.VISIBLE
                dataAdapter.updateCountries(it)
            }
        })

        viewModel.progress.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {

                    dataRecyclerView.visibility = View.GONE
                }
            }
        })
    }

    class DataAdapter(var datas: ArrayList<Data>): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

        fun updateCountries(newUsers: List<Data>) {
            datas.clear()
            datas.addAll(newUsers)
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
                imageView.loadImage(data.avatar)
            }
        }
    }
}
package com.example.pos.Model

 class model_harian(val tanggal:String, val bulan:String, val tahun: String, ChildItemList: ArrayList<model_harian_detail>) {
    private var ChildItemList: ArrayList<model_harian_detail>
    fun getChildItemList(): ArrayList<model_harian_detail>{
        return ChildItemList
    }

    fun setChildItemList(
        childItemList: ArrayList<model_harian_detail>
    ) {
        ChildItemList = childItemList
    }

    // Constructor of the class
    // to initialize the variables
    init {
        this.ChildItemList = ChildItemList
    }
}
package framework.telegram.business.ui.search.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.trello.rxlifecycle3.android.ActivityEvent
import framework.ideas.common.model.contacts.ContactDataModel
import framework.telegram.business.bridge.bean.AccountInfo
import framework.telegram.business.db.RealmCreator
import framework.telegram.business.ui.search.contract.SearchContract
import framework.telegram.support.account.AccountManager
import io.reactivex.Observable
import io.realm.Case
import io.realm.Realm
import io.realm.Sort

class SearchNewCallPresenterImpl : SearchContract.Presenter {

    private var mContext: Context?
    private var mView: SearchContract.View
    private var mObservalbe: Observable<ActivityEvent>

    constructor(view: SearchContract.View, context: Context?, observable: Observable<ActivityEvent>) {
        this.mView = view
        this.mObservalbe = observable
        this.mContext = context
        view.setPresenter(this)
    }

    override fun start() {

    }

    @SuppressLint("CheckResult")
    override fun getDataSearchList(keyword: String, dataList: MutableList<MultiItemEntity>,pageNum:Int) {
        if (!TextUtils.isEmpty(keyword)){
            var models: Iterable<ContactDataModel>?
            val myUid = AccountManager.getLoginAccount(AccountInfo::class.java).getUserId()
            RealmCreator.executeContactsTransactionAsync(myUid, { realm ->
                models = realm.where(ContactDataModel::class.java)
                        .like("nickName", "*$keyword*", Case.INSENSITIVE)
                        ?.or()
                        ?.like("noteName", "*$keyword*", Case.INSENSITIVE)
                        ?.sort("letter", Sort.ASCENDING)?.findAll()

                val list = mutableListOf<MultiItemEntity>()
                models?.forEach {
                    list.add(it.copyContactDataModel())
                }
                dataList.addAll(list)
            }, {
                mView.getDataListSuccess(dataList,false)
            })
        }else{
            mView.getDataListSuccess(mutableListOf(),false)
        }
    }

    override fun destroy() {
    }
}


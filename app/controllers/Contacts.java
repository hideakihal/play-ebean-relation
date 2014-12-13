package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

import java.util.*;

import models.*;
import play.data.validation.Constraints.Required;

import com.avaje.ebean.Page;

public class Contacts extends Controller {

	public static class FindForm {
		@Required
		public String input;
	}
	
	public static Result index() {
		Page<Member> datas = Member.page(0, 10, "id", "desc", "");
		return ok(indexuser.render("データベースのサンプル", datas));
	}

	public static Result list(Integer page, Integer size, String sortBy, String filter){
		Page<Member> datas = Member.page(page, size, sortBy, "desc", filter);
		return ok(indexuser.render("ユーザのデータベース", datas));
	}

    public static Result add() {
    	Form<Member> f = new Form(Member.class);
    	return ok(adduser.render("ユーザフォーム",f));
    }

    public static Result create() {
    	Form<Member> f = new Form(Member.class).bindFromRequest();
    	if (!f.hasErrors()) {
    		Member data = f.get();
    		data.save();
    		return redirect("/indexuser");
    	} else {
    		return badRequest(adduser.render("ERROR",f));
    	}
    }
    
    public static Result view(Long id) {
        Form<Member> f = new Form(Member.class).fill(
        		Member.find.byId(id)
       );
       return ok(viewuser.render("ID=" + id + "の投稿を表示。", id, f));
    }
    
    public static Result edit(Long id) {
        Form<Member> f = new Form(Member.class).fill(
        		Member.find.byId(id)
       );
       return ok(edituser.render("ID=" + id + "の投稿を編集。", id, f));
    }
    
    public static Result update() {
    	Form<Member> f = new Form(Member.class).bindFromRequest();
    	if (!f.hasErrors()) {
    		Member data = f.get();
    		/*
    		 *  bindFromRequestはフォームに入力しPOSTされた値しか取得されず、
    		 *  関連したMessageオブジェクトは送信されない。
    		 *  そのため、f.getで取得されたMemberオブジェクトにはMessageオブジェクト
    		 *  が含まれないため、Updataするとオブジェクトが削除された状態で
    		 *  更新される。
    		 *  そこで、MemberのIDからオブジェクトを取得することで、関連したMessage
    		 *  オブジェクトを取得し、再度追加を行ってUpdataする。
    		 */
    		Member name = Member.findById(data.id.toString());
    		List<Message> arr = name.messages;
    		for(Message m : arr){
    			data.messages.add(m);
    		}
    		data.update();
    		return redirect("/indexuser");
    	} else {
    		return ok(adduser.render("ERROR:再度入力してください。",f));
    	}
    }
    
    public static Result delete(Long id) {
        Form<Member> f = new Form(Member.class).fill(
        		Member.find.byId(id)
       );
    	if (!f.hasErrors()) {
    		Member data = f.get();
    		if(data != null){
    			data.delete();
    			return redirect("/indexuser");
    		} else {
    			return ok(adduser.render("ERROR:そのID番号は見つかりません。",f));
    		}
    	} else {
    		return ok(adduser.render("ERROR:入力にエラーが起こりましたt。",f));
    	}
    }
}

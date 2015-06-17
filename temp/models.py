#!/usr/bin/env python
# coding=utf-8

import os.path
import tornado.web


class HTTP404Error(tornado.web.ErrorHandler):
    def initialize(self):
        tornado.web.ErrorHandler.initialize(self, 404)

    def prepare(self):
        self.render('404.html')

class SignupHandler(tornado.web.RequestHandler):
	def get(self):
		id = self.get_secure_cookie("id")
		if id:
			self.redirect('/')
		else:
			self.render('reg.html')

	def post(self):
		user = self.get_argument('user', None)
		password = self.get_argument('password', None)
		repassword = self.get_argument('repassword', None)
		name = self.get_argument('name',None)
		email = self.get_argument('email',None)
		qq = self.get_argument('qq', None)
		if user == None:
			self.redirect('/signup')
			return
		if password == None or repassword == None or not password == repassword:
			self.redirect('/signup')
			return
		if name == None:
			self.redirect('/signup')
			return
		if email == None:
			self.redirect('/signup')
			return
		'''
		check and save data
		'''
		self.set_secure_cookie("id", user)
		self.set_secure_cookie("name", name)
		self.redirect('/')
		
class LoginHandler(tornado.web.RequestHandler):
	def get(self):
		id = self.get_secure_cookie("id")
		name = self.get_secure_cookie("name")
		if id:
			self.redirect('/')
		else:
			self.render('login.html')

	def post(self):
		user = self.get_argument('user', None)
		password = self.get_argument('password', None)
		if user: #check information if true
		    name = "name" #get name
		    self.set_secure_cookie("id", user)
		    self.set_secure_cookie("name", name)
		    self.redirect('/')
		else:
			self.redirect('/login')

class IndexHandler(tornado.web.RequestHandler):
	def get(self):
	    self.render('index.html')

class UpdateUserInfoHandler(tornado.web.RequestHandler):
	def get(self):
		name = self.get_secure_cookie("name")
		id = self.get_secure_cookie("id")
		self.render('change_info.html', name = name,id = id)
	
	def post(self):
		id = self.get_secure_cookie("id")
		new_name = self.get_argument('userName')
		old_password = self.get_argument('orginalPassword')
		new_password = self.get_argument('newPassword')
		new_repassword = self.get_argument('password-confirm')
		new_mail = self.get_argument('email')
		new_qq = self.get_argument('qq')
		if id: #check id and old_password if true
		    #update information
		    self.redirect('/profile/'+str(id))

class LogoutHandler(tornado.web.RequestHandler):
	def get(self):
		self.set_secure_cookie("id", '')
		self.set_secure_cookie("name", '')
		self.redirect('/')

class MailboxHandler(tornado.web.RequestHandler):
	def get(self):
		id = self.get_secure_cookie("id")
		name = self.get_secure_cookie("name")
		txts = [(1000,"title1",1000,"author1"),(1001,"title2",1001,"author1")]#get 5 random Article for (artid,title,userid,name)
		if id:
		    self.render('read_message.html',id = id, name = name, allTxts = txts)
		else:
		    self.redirect('/login')


class ArticleHandler(tornado.web.RequestHandler):
	def get(self, ArticleID):
	    id = self.get_secure_cookie("id")
	    name = self.get_secure_cookie("name")
	    if ArticleID:
		    art_info = {"Title":"Title","Artid":"ArticleID","Content":"Content","Autid":"Autid","Autname":"Autname","Autmail":"Autmail","Autqq":"Autqq"}#get those information
		    self.render('article.html', name = name,id = id,txt = art_info)
	    else:
		    self.redirect('/error')

        

class UserInfoHandler(tornado.web.RequestHandler):
	def get(self, UserID):
		name = self.get_secure_cookie("name")
		mail = "mail" #get mail
		qq = UserID #get qq
		artlist = [(1000,'ART1'),(1001,"ART2")]#from userid get list for (artid, title)
		muslist = [(1000,'mus1'),(1001,'mus2')]
		if name:
			self.render('user_info.html', name = name, mail = mail, qq = qq, id = UserID,arts = artlist,muss = muslist)
		else:
			self.redirect('/error')

class MuseumHandler(tornado.web.RequestHandler):
	def get(self):
		name = self.get_secure_cookie("name")
		id = self.get_secure_cookie("id")
		exh = [{"src":"{{static_url(\"img/model1.jpg\")}}","content":"Test1"},{"src":"{{static_url(\"img/model1.jpg\")}}","content":"Test2"}]
		self.render('imageItem.html', name = name, id =id, allImageItem = exh)

class AdminLoginHandler(tornado.web.RequestHandler):
    def get(self):
        id = self.get_secure_cookie("id")
        if id:
            self.redirect('/')
        else:
            self.render('adminLogin.html')

    def post(self):
        id =self.get_argument("id")
        paw = self.get_argument("password")
        if id == "admin" and paw == "admin":
            self.set_secure_cookie("id", id)
            self.set_secure_cookie("name", 'admin')
            self.redirect('/')
        else:
            self.redirect('/adminlogin')
            
class AdminHandler(tornado.web.RequestHandler):
    def get(self):
        id = self.get_secure_cookie('id')
        if id == "admin":
            artlist= [{"title":"t1","id":1000},{"title":"t2","id":1001}]#all article title and id
            self.render('adminManage.html', allArticleItem = artlist)
        else:
            self.redirect('/')

class NewblogHandler(tornado.web.RequestHandler):
    def get(self):
        id = self.get_secure_cookie('id')
        name = self.get_secure_cookie('name')
        if id:
            self.render('createArticleItem.html', name= name, id = id)
        else:
            self.redirect('/login')
            
    def post(self):
        id = self.get_secure_cookie('id')
        title = self.get_argument("title")
        content = self.get_argument("content")
        mode = self.get_argument("mode")
        if title and content and mode:
            artid = "1000" #save article and return article id
            self.redirect('/article/'+artid)
        
class NewexhHandler(tornado.web.RequestHandler):
    def get(self):
        self.render('createImageItem.html')


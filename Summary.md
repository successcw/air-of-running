# 2012/12/19 #

## 解决eclipse中文乱码 ##
  * windows-->preferences-->General-->Workspace-->选择Text file encoding中的Other，选择GBK，如果没有直接输入GBK，点击“Apply”
  * windows-->preferences-->General-->Content Types-->点击右边窗口中的Text，选择Java Source File，在Default encoding【在窗口最下边，如果看不到，拖动滑块下拉即可看到】中输入GBK，点击OK。
  * 来源：http://blog.csdn.net/config_man/article/details/5739613

# 2012/12/20 #

## git使用代理 ##
  * git config http.proxy https://127.0.0.1:8087 （goagent代理地址)

## git 使用记录 ##
  * git add .
  * git commit -m "注释"
  * git remote add googlecode https://code.google.com/p/air-of-running/
  * git push googlecode master:master
  * git pull googlecode master

## smali语法 ##
  * http://my.oschina.net/xiahuawuyu/blog/57146

# 2012/12/24 #
## android颜色参考 ##
  * http://www.ceveni.com/2009/08/set-rgb-color-codes-for-android-and-rgb.html
  * example: USAQIVALUEView.setTextColor(Color.rgb(176,48,96));

# 2012/12/27 #
## PM2.5相关资料 ##
  * 《世界卫生组织关于颗粒物、臭氧、二氧化氮和二氧化硫的空气质量准则》2005 年全球更新版
> > http://apps.who.int/iris/bitstream/10665/69477/3/WHO_SDE_PHE_OEH_06.02_chi.pdf
  * PM2.5 中美标准对比
> > 见download

# 2013/01/30 #
## cloudfoundry ##
  * cloudfoundry是vmc提供的开源云平台，只需在eclipse里安装对应的插件即可操作
  * vmc是命令行工具，需要安装如下包：
    1. uby
    1. ubygems
    1. pt-get install libyaml-dev
    1. udo gem install vmc
  * vmc获取app log
    1. mc target https://api.cloudfoundry.com
    1. mc login
    1. mc files airofrunning-server logs

## cloudfoundry绑定mysql ##
  * eclipse->servers->application->services->add services 选择mysql
  * 将建立好的mysql托到对应的application上就可以完成绑定
  * 右键点击刚建立好的mysql,选择open tunnel,完成之后会出现账户，密码端口等信息，这个主要用于本地访问mysql, 如：mysql workbench tool，eclipse->data source explorer
  * 下面以命令行工具为例：<br>mysql -u ufVVSzZxviCB3 -P 10100 -D d1fd349dc26d74d0ab4bd8a244cd0973e -p  -h 127.0.0.1</li></ul>

<ul><li>中文乱码的解决：<br>通过上面那个命令登入之后，下命令：show variables like 'char%';<br>mysql> show variables like 'char%';<br>
</li></ul><blockquote><table><thead><th>+--------------------------+----------------------------+</th></thead><tbody>
<tr><td> Variable_name            </td><td> Value                     </td></tr>
<tr><td>+--------------------------+----------------------------+</td></tr>
<tr><td> character_set_client     </td><td> utf8                       </td></tr>
<tr><td> character_set_connection </td><td> utf8                       </td></tr>
<tr><td> character_set_database   </td><td> utf8                       </td></tr>
<tr><td> character_set_filesystem </td><td> binary                     </td></tr>
<tr><td> character_set_results    </td><td> utf8                       </td></tr>
<tr><td>character_set_server     </td><td> utf8                       </td></tr>
<tr><td> character_set_system     </td><td> utf8                       </td></tr>
<tr><td> character_sets_dir       </td><td> /usr/share/mysql/charsets/ </td></tr>
<tr><td>+--------------------------+----------------------------+</td></tr>
通过上面的返回表格可以看出，cloudfoundry上编码都为utf8,所以客户端使用utf-8即可<br>例如：<br>SQL = “中文”<br>SQL = new String(SQL.getBytes(),"UTF-8"); //转换为utf-8<br>如果要在代码中连接上面建立好的mysql，需要使用如下函数来获取相应的用户名和密码（不能使用open tunnel后出现的用户名和密码）<br>String srvInfo = System.getenv("VCAP_SERVICES");</blockquote></tbody></table>


<h1>2013/03/28</h1>
<h2>ppb->ug/m3</h2>
<ul><li><a href='http://www.epa.ie/whatwedo/monitoring/air/standards/'>http://www.epa.ie/whatwedo/monitoring/air/standards/</a>
<h2>美国环保法案</h2>
</li><li><a href='http://www.gpo.gov/fdsys/pkg/FR-2013-01-15/pdf/2012-30946.pdf'>http://www.gpo.gov/fdsys/pkg/FR-2013-01-15/pdf/2012-30946.pdf</a></li></ul>

<h1>2013/04/09</h1>
<h2>格式化code,去掉行首行尾多余空格</h2>
<ul><li>ctrl + shift + f 格式化代码
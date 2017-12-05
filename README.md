# um_open
UM Open monitoring

Назначение: сбор данных и мониторинг любых информационных систем.
#Для um_open_web_GUI
Перед первым стартом проекта необходимо в папке \um_open\WEB-INF\views\
создать подпапку \config
и в ней файл setting.cfg с содержанием

## LoginType - это метод проверки логина, Может быть {DB | LDAP}
LoginType=DB
## FileNameDB - наменование файла в папке \view\config\... в этом примере для БД SQLite - sqlite_connection.cfg
FileNameDB=sqlite_connection.cfg
## FileNameLDAP - наменование файла в папке \view\config\... в этом примере для LDAP - ldap_connection.cfg
FileNameLDAP=ldap_connection.cfg

#Для um_open_scheduler
перед запуском необходимо создать
если вы работаете с БД Oracle
\scheduler\config\oracle_connection.cfg 
с содержанием
# имя пользователя БД
DB_UserName=
# пароль пользователя БД
DB_Password=
# URL JDBC подключения к БД
DB_URL_Connection1=jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=oracle.service)))
# путь к JDBC драйверу
Driver_Path=c:\\Server\\Tomcat\\webapps\\um\\WEB-INF\\lib\\ojdbc7.jar
# имя класса JDBC драйвера
ClassDriverName=oracle.jdbc.OracleDriver

или если вы работаете с БД Sqlite
\scheduler\config\sqlite_connection.cfg

Для запуска um_open_scheduler как сервиса в ОС можно воспользоваться 
java wrapper community версией
https://wrapper.tanukisoftware.com/doc/english/download.jsp

входные параметры scheduler
# тип языка RUS|ENG 
Lang=RUS
# наименование БД
DBMS=SQlite
# путь к файлу с конфигурацией подключения к БД
FileName=C:\Server\Repositories\Projects\um_open\scheduler\config\sqlite_connection.cfg

Пример файла создания службы для ОС Windows:
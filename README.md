# um_open
UM Open monitoring

Назначение: сбор данных и мониторинг различных информационных систем.
Перед первым стартом проекта необходимо в папке \um_open\WEB-INF\views\создать подпапку \configи в ней файл setting.cfg с содержанием

## LoginType - это метод проверки логина, Может быть {DB | LDAP}
LoginType=DB
## FileNameDB - наменование файла в папке \view\config\... в этом примере для БД SQLite - sqlite_connection.cfg
FileNameDB=sqlite_connection.cfg
## FileNameLDAP - наменование файла в папке \view\config\... в этом примере для LDAP - ldap_connection.cfg
FileNameLDAP=ldap_connection.cfg

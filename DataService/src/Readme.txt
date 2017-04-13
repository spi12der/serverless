Parameters required to call this service:

All this parameters inside JSONObject("parameters")

For Create:
1. type = create
2. db_name
3. tbl_name
4. attributes
5. attributes_type
returns status = 1 | 0, message

For Insert:
1. type = insert
2. db_name
3. tbl_name
4. attributes
5. values
returns status = 1 | 0, message

For Select:
1. type = select
2. db_name
3. tbl_name
4. attributes(comma separated)
5. conditions(# separated conditions, Ex: a~10#b~20)
returns status = 1 | 0, message, result(JSONArray of each row)

Database : admin
Table : sd_details(user_type, username, password, email_id)
Table : username(services)
from flask import Flask, request, jsonify, current_app
import mysql.connector
import time
application=Flask(__name__)

db=mysql.connector.connect(
    host="127.0.0.1",
    user="root",
    password="password",
    database="madcamp"
)

cursor=db.cursor(buffered=True)

@application.route("/handle_post_request", methods=['POST'])

def handle_post_request():
    
    data = request.form.get('data')
    print(type(data))

    query = "INSERT INTO users (nickname) VALUES (%s)"
    values = (data,)
    cursor.execute(query, values)
    db.commit()
    
    return "Hello from Flask (POST)" , 202

@application.route("/save_userinfo", methods=['POST'])
def save_userinfo():
    try: 
        id = request.form.get('id')
        nickname = request.form.get('nickname')
        print(id, nickname)

        query="INSERT INTO users (user_id, nickname) VALUES (%s, %s)"
        values = (id, nickname)
        cursor.execute(query, values)
        db.commit()
    except Exception as e:
        error_message = f"Error : {str(e)}"
        print(error_message)
        return error_message, 500
    
    return "Hello from Flask (POST)" , 202
    

@application.route("/check_available_seats", methods=['POST'])
def check_available_seats():
    try:
        data = request.get_json()

        print(data)

        hour1=data['hour1']
        minute1=data['minute1']
        hour2=data['hour2']
        minute2=data['minute2']
        start_time = hour1+":"+minute1+":00"
        end_time = hour2+":"+minute2+":00"

        queryA = "SELECT COUNT(DISTINCT seat_no) as seatA_count FROM occupied_seatsA WHERE (start_time<='"+start_time+"' AND end_time>='"+end_time+"') OR (start_time>'"+start_time+"' AND start_time<'"+end_time+"') OR (end_time<'"+start_time+"' AND end_time<'"+end_time+"')"
        queryB = "SELECT COUNT(DISTINCT seat_no) as seatB_count FROM occupied_seatsB WHERE (start_time<='"+start_time+"' AND end_time>='"+end_time+"') OR (start_time>'"+start_time+"' AND start_time<'"+end_time+"') OR (end_time<'"+start_time+"' AND end_time<'"+end_time+"')"
        queryC = "SELECT COUNT(DISTINCT seat_no) as seatC_count FROM occupied_seatsC WHERE (start_time<='"+start_time+"' AND end_time>='"+end_time+"') OR (start_time>'"+start_time+"' AND start_time<'"+end_time+"') OR (end_time<'"+start_time+"' AND end_time<'"+end_time+"')"


        cursor.execute(queryA)
        resultA = cursor.fetchone()[0]
        cursor.execute(queryB)
        resultB = cursor.fetchone()[0]
        cursor.execute(queryC)
        resultC = cursor.fetchone()[0]
        db.commit()

        return jsonify({'resultA': resultA, 'resultB': resultB, 'resultC': resultC, 'message': 'Occupied excess successfully'})
    except Exception as e:
        return jsonify({'error': str(e)})


    
@application.route("/room_state", methods=['POST'])
def room_state():
    try:
        data = request.get_json()


        start_time=data['start_time']
        end_time=data['end_time']
        room=data['room']
        user_id = data['user_id']

        query = "SELECT DISTINCT seat_no as not_available FROM occupied_seats"+room+" WHERE (start_time<='"+start_time+"' AND end_time>='"+end_time+"') OR (start_time>'"+start_time+"' AND start_time<'"+end_time+"') OR (end_time<'"+start_time+"' AND end_time<'"+end_time+"')"
        query1 = "SELECT seat_no FROM occupied_seats"+room+" WHERE user_id="+user_id


        cursor.execute(query)
        result_set = cursor.fetchall()
        not_available = [result[0] for result in result_set]

        cursor.execute(query1)
        result_seat = cursor.fetchone()
        my_seat = "no"

        if result_seat :
            my_seat = str(result_seat[0])

        return jsonify({'not_available': not_available, 'my_seat' : my_seat})
    except Exception as e:
        return jsonify({'error': str(e)})
    
@application.route("/show_reservation_info", methods=['POST'])
def show_reservation_info():
    try:
        data = request.get_json()

        print(data)

        user_id=data['user_id']

        query_room = "SELECT room FROM occupied_users WHERE user_id = %s"
        cursor.execute(query_room, (user_id,))
        room = cursor.fetchone()


        if room:
            # 가져온 Room 값에 해당하는 occupied_seats 테이블의 이름 생성
            table_name = "occupied_seats" + str(room[0])

            # occupied_seats 테이블에서 user_id에 해당하는 행의 start_time과 end_time 값을 가져옴
            query_times = "SELECT start_time, end_time,seat_no FROM " + table_name + " WHERE user_id = %s"
            cursor.execute(query_times, (user_id,))
            result = cursor.fetchone()

            if result:
                start_time, end_time, seat_no = result
                print(result)
                start_str = "{:02}:{:02}:{:02}".format(start_time.seconds // 3600, (start_time.seconds % 3600) // 60, start_time.seconds % 60)
                end_str = "{:02}:{:02}:{:02}".format(end_time.seconds // 3600, (end_time.seconds % 3600) // 60, end_time.seconds % 60)
                end_time_lst=end_str.split(":")
                start_time_lst=start_str.split(":")
                print(start_str)
                return jsonify({'seat_info': room[0]+ str(seat_no),'start_hour': start_time_lst[0],'start_min': start_time_lst[1], 'end_hour': end_time_lst[0],'end_min': end_time_lst[1]})
            else:
                return jsonify({'start_time': "해당값이 없음", 'end_time': "해당값이 없음", 'seat_info':"해당값이 없음"})
        else:
            return jsonify({'start_time': "해당값이 없음", 'end_time': "해당값이 없음", 'seat_info':"해당값이 없음"})

    except Exception as e:
        return jsonify({'error': str(e)})
    

@application.route("/delete_reservation", methods=['POST'])
def delete_reservation():
    try:
        data = request.get_json()

        print(data)

        user_id=data['user_id']
        
        check_reservation_query = "SELECT room FROM occupied_users WHERE user_id ="+user_id
        cursor.execute(check_reservation_query)
        room = cursor.fetchone()[0]

        if room :
            query1 = "DELETE FROM occupied_users WHERE user_id ="+ user_id
            query2 = "DELETE FROM occupied_seats"+room+" WHERE user_id ="+ user_id
            cursor.execute(query1)
            cursor.execute(query2)
            method = "YES"
        else :
            method = "NO"

        return jsonify({'Delete_method': method})
    except Exception as e:
        return jsonify({'error': str(e)})
    

@application.route("/insert_reservation", methods=['POST'])
def insert_reservation():
    try:
        data = request.get_json()

        print(data)

        start_time=data['start_time']
        end_time=data['end_time']
        room=data['room']
        seat_no=data['seat_no']
        user_id=data['user_id']
        
        check_reservation_query = "SELECT room FROM occupied_users WHERE user_id ="+user_id
        cursor.execute(check_reservation_query)
        pre_room = cursor.fetchone()

        if pre_room :
            query1 = "UPDATE occupied_users SET room = '"+room+"', seat_no ="+ seat_no+" WHERE user_id ="+ user_id
            query2 = "INSERT INTO occupied_seats"+room+"(user_id,seat_no,start_time,end_time) VALUES("+user_id+","+seat_no+",'"+start_time+"','"+end_time+"')"
            if pre_room[0]==room :
                query3 = "UPDATE occupied_seats"+room+" SET seat_no = "+seat_no+", start_time = '"+start_time+"', end_time = '"+end_time+"' WHERE user_id ="+user_id
                cursor.execute(query1)
                cursor.execute(query3)
            else :
                query2 = "INSERT INTO occupied_seats"+room+"(user_id,seat_no,start_time,end_time) VALUES("+user_id+","+seat_no+",'"+start_time+"','"+end_time+"')"
                query3 = "DELETE FROM occupied_seats"+pre_room[0]+" WHERE user_id="+user_id
                cursor.execute(query1)
                cursor.execute(query2)
                cursor.execute(query3)
            method = "update"
        else :
            query1 = "INSERT INTO occupied_users(user_id,room,seat_no) VALUES("+user_id+",'"+room+"',"+seat_no+")"
            query2 = "INSERT INTO occupied_seats"+room+"(user_id,seat_no,start_time,end_time) VALUES("+user_id+","+seat_no+",'"+start_time+"','"+end_time+"')"
            cursor.execute(query1)
            cursor.execute(query2)
            method = "insert"

        return jsonify({'Reservation_method': method})
    except Exception as e:
        return jsonify({'error': str(e)})
    

if __name__ == "__main__":
    application.run(host='0.0.0.0', port=80)


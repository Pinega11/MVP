package com.example.myapplication1.ui.database

import android.annotation.SuppressLint
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.TextView
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class ConSQL {
    var con: Connection? = null

    data class ClientData(
        val surname: String,
        val name: String,
        val patronomyc: String,
        val phone: String,
        val address: String
    )

    @SuppressLint("NewApi")
    fun conclass(): Connection? {
        val ip = "192.168.0.6"
        val port = "1433"
        val db = "conders"
        val username = "sa"
        val password = "123"
        val a = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(a)
        var connectURL: String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            connectURL =
                "jdbc:jtds:sqlserver://$ip:$port;databasename=$db;user=$username;password=$password;"
            con = DriverManager.getConnection(connectURL)
        } catch (e: Exception) {
            Log.e("Error :", e.message!!)
        }
        return con
    }

    fun loginUser(phone: String, password: String): Boolean {
        val sql = "SELECT * FROM clients WHERE phone = ? AND password = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            preparedStatement?.setString(2, password)
            resultSet = preparedStatement?.executeQuery()

            return resultSet?.next() ?: false
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return false
    }

    fun insertClient(
        familia: String,
        name: String,
        otchestvo: String,
        gender: String,
        phone: String,
        password: String,
        address: String,
        email: String
    ) {
        val sql = "INSERT INTO clients (surname, name, patronomyc, gender, phone, password, address, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, familia)
            preparedStatement?.setString(2, name)
            preparedStatement?.setString(3, otchestvo)
            preparedStatement?.setString(4, gender)
            preparedStatement?.setString(5, phone)
            preparedStatement?.setString(6, password)
            preparedStatement?.setString(7, address)
            preparedStatement?.setString(8, email)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun getClientFIO(phone: String): String? {
        val sql = "SELECT surname, name, patronomyc FROM clients WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                val surname = resultSet.getString("surname")
                val name = resultSet.getString("name")
                val patronomyc = resultSet.getString("patronomyc")
                return "$surname ${name.firstOrNull()?.toUpperCase()}. ${patronomyc.firstOrNull()?.toUpperCase()}."
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    fun getClientData(phone: String): ClientData? {
        val sql = "SELECT surname, name, patronomyc, phone, address FROM clients WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                val surname = resultSet.getString("surname")
                val name = resultSet.getString("name")
                val patronomyc = resultSet.getString("patronomyc")
                val clientPhone = resultSet.getString("phone")
                val clientAddress = resultSet.getString("address")

                return ClientData(surname, name, patronomyc, clientPhone, clientAddress)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    fun insertOrder(
        fio: String,
        phone: String,
        address: String,
        floor: String,
        avtocar: String,
        type_of_room: String,
        condition_of_room: String,
        status: String
    ) {
        val sql = "INSERT INTO orders (fio, phone, address, floors, avtocar, type_of_room, condition_of_room, cost_of_work, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, fio)
            preparedStatement?.setString(2, phone)
            preparedStatement?.setString(3, address)
            preparedStatement?.setString(4, floor)
            preparedStatement?.setString(5, avtocar)
            preparedStatement?.setString(6, type_of_room)
            preparedStatement?.setString(7, condition_of_room)

            val costOfWork = when {
                condition_of_room == "Отделка" -> 12000
                condition_of_room == "Ремонт" -> 15000
                else -> 0
            }

            preparedStatement?.setString(8, costOfWork.toString())
            preparedStatement?.setString(9, status)
            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    data class ConderInfo(val firma: String, val model: String) {
    }

    fun getConderBySquare(square: Double): ConderInfo? {
        val sql = "SELECT firm, model FROM conditioners WHERE ? BETWEEN min_square AND max_square"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setDouble(1, square)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                val firma = resultSet.getString("firm")
                val model = resultSet.getString("model")
                return ConderInfo(firma, model)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    fun updateOrderSquare(phone: String, square_of_room: String) {
        val sql = "UPDATE orders SET square_of_room = ? WHERE phone = ? AND status = 0"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, square_of_room)
            preparedStatement?.setString(2, phone)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun getSquareFromDatabase(phone: String): String? {
        val sql = "SELECT square_of_room FROM orders WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                return resultSet.getString("square_of_room")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    fun getConditionersBySquare(square: Double): List<ConderInfo> {
        val sql = "SELECT firm, model FROM conditioners WHERE ? BETWEEN min_square AND max_square"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        val conditionersList = mutableListOf<ConderInfo>()

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setDouble(1, square)
            resultSet = preparedStatement?.executeQuery()

            while (resultSet?.next() == true) {
                val firma = resultSet.getString("firm")
                val model = resultSet.getString("model")
                conditionersList.add(ConderInfo(firma, model))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }

        return conditionersList
    }

    fun updateOrderConder(phone: String, conditioner: String) {
        val sql = "UPDATE orders SET conditioner = ? WHERE phone = ? AND status = 0"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, conditioner)
            preparedStatement?.setString(2, phone)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun getConditionerCost(conditioner: String): String? {
        val sql = "SELECT cost FROM conditioners WHERE CONCAT(firm, ' ', model) = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, conditioner)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                return resultSet.getString("cost")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    data class OrderData(
        val fio: String,
        val phone: String,
        val address: String,
        val floors: String,
        val avtocar: String,
        val square_of_room: String,
        val type_of_room: String,
        val condition_of_room: String,
        val conditioner: String
    )

    fun getOrderData(phone: String): OrderData? {
        val sql = "SELECT fio, phone, address, floors, avtocar, square_of_room, type_of_room, condition_of_room, conditioner FROM orders WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                val orderFio = resultSet.getString("fio")
                val orderPhone = resultSet.getString("phone")
                val orderAddress = resultSet.getString("address")
                val orderFloors = resultSet.getString("floors")
                val orderAvtocar = resultSet.getString("avtocar")
                val orderSquare_of_room = resultSet.getString("square_of_room")
                val orderType_of_room = resultSet.getString("type_of_room")
                val orderCondition_of_room = resultSet.getString("condition_of_room")
                val orderConditioner = resultSet.getString("conditioner")

                return OrderData(orderFio, orderPhone, orderAddress, orderFloors, orderAvtocar, orderSquare_of_room, orderType_of_room, orderCondition_of_room, orderConditioner)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    fun checkOrderExistenceWithStatus(phone: String, status: String): Boolean {
        val sql = "SELECT COUNT(*) as count FROM orders WHERE phone = ? AND status = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            preparedStatement?.setString(2, status)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                val count = resultSet.getInt("count")
                return count > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return false
    }

    data class Order(
        val fio: String,
        val number_order: String,
        val phone: String,
        val address: String,
        val conditioner: String,
        val cost_of_work: String,
        val status: String
    )

    fun getOrdersDataByPhone(phone: String): List<Order> {
        val sql = "SELECT number_order, fio, phone, address, conditioner, cost_of_work, status FROM orders WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        val ordersList = mutableListOf<Order>()

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            resultSet = preparedStatement?.executeQuery()

            while (resultSet?.next() == true) {
                val orderFIO = resultSet.getString("fio")
                val orderNumber = resultSet.getString("number_order")
                val orderPhone = resultSet.getString("phone")
                val orderConditioner = resultSet.getString("conditioner")
                val orderAddress = resultSet.getString("address")
                val orderCostOfWork = resultSet.getString("cost_of_work")
                val orderStatus = resultSet.getString("status")

                val orderData = Order(orderFIO, orderNumber, orderPhone, orderAddress, orderConditioner, orderCostOfWork, orderStatus)
                ordersList.add(orderData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }

        return ordersList
    }

    fun updateOrder(phone: String, numberOrder: String, date_of_reg_application: String) {
        val sql = "UPDATE orders SET number_order = ?, date_of_reg_application = CONVERT(DATETIME, ?, 120) WHERE phone = ? AND status = 0"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, numberOrder)
            preparedStatement?.setString(2, date_of_reg_application)
            preparedStatement?.setString(3, phone)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun updatePhotoUrlInside(phone: String, imageUrlInside: String) {
        val sql = "UPDATE orders SET photo_inside = ? WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, imageUrlInside)
            preparedStatement?.setString(2, phone)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun deleteOrder(phone: String, orderNumber: String) {
        val sql = "DELETE FROM orders WHERE phone = ? AND number_order = ?"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            preparedStatement?.setString(2, orderNumber)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun getCostOfWorkFromDatabase(phone: String): String? {
        val sql = "SELECT cost_of_work FROM orders WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)
            resultSet = preparedStatement?.executeQuery()

            if (resultSet?.next() == true) {
                return resultSet.getString("cost_of_work")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            con?.close()
        }
        return null
    }

    fun updateOrderCost(phone: String, cost: String) {
        val sql = "UPDATE orders SET cost_of_work = ? WHERE phone = ? AND status = 0"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, cost)
            preparedStatement?.setString(2, phone)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }

    fun deleteOrderButton(phone: String) {
        val sql = "DELETE FROM orders WHERE phone = ?"
        var preparedStatement: PreparedStatement? = null

        try {
            con = conclass()
            preparedStatement = con?.prepareStatement(sql)
            preparedStatement?.setString(1, phone)

            preparedStatement?.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            preparedStatement?.close()
            con?.close()
        }
    }
}
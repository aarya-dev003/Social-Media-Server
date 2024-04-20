package com.example.routes

import com.example.model.collegeAdmin.announcement.Announcement
import com.example.model.collegeAdmin.announcement.AnnouncementDataSource
import com.example.utils.Constants.CREATE_ANNOUNCEMENT
import com.example.utils.Constants.DELETE_ANNOUNCEMENT
import com.example.utils.Constants.GET_ANNOUNCEMENT
import com.example.utils.Constants.GET_ANNOUNCEMENT_CLUB
import com.example.utils.Constants.GET_ANNOUNCEMENT_USER
import com.example.utils.Constants.UPDATE_ANNOUNCEMENT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.announcementRoutes(
    announcementDataSource : AnnouncementDataSource
) {
    // Announcements Routes
   authenticate("college-jwt"){
       post(CREATE_ANNOUNCEMENT){
           val request = kotlin.runCatching {
               call.receiveNullable<Announcement>()} .getOrNull() ?: kotlin.run {
               call.respond(HttpStatusCode.BadRequest, "Unable to get Data Class")
               return@post
           }


           val wasAcknowledged = announcementDataSource.createAnnouncement(request)
           if (!wasAcknowledged) {
               call.respond(HttpStatusCode.Conflict, "Unable to create Announcement")
               return@post
           }
           call.respond(HttpStatusCode.OK, "Announcement was created")
       }

       put(UPDATE_ANNOUNCEMENT){
           val request = kotlin.runCatching {
               call.receiveNullable<Announcement>()} .getOrNull() ?: kotlin.run {
               call.respond(HttpStatusCode.BadRequest, "Unable to get Data Class")
               return@put
           }



           val wasAcknowledged = announcementDataSource.updateAnnouncement(request)
           if (!wasAcknowledged) {
               call.respond(HttpStatusCode.Conflict, "Unable to update Announcement")
               return@put
           }
           call.respond(HttpStatusCode.OK, "Announcement was updated")

       }

       delete(DELETE_ANNOUNCEMENT){
           val announcementId= call.parameters["id"]
           if (announcementId == null) {
               call.respond(HttpStatusCode.BadRequest, "ID parameter is missing")
               return@delete
           }

           try {

               val announcement= announcementDataSource.deleteAnnouncement(announcementId)

               if(!announcement){
                   call.respond(HttpStatusCode.Conflict, "Announcement not Deleted")
               }

               call.respond(HttpStatusCode.OK, "Announcement deleted successfully")
           } catch (e: IllegalArgumentException) {
               call.respond(HttpStatusCode.BadRequest, "Invalid ObjectId provided")
           } catch (e: Exception) {
               call.respond(HttpStatusCode.InternalServerError, "Failed to delete post: ${e.message}")
           }
       }

       get(GET_ANNOUNCEMENT){
           try {
               val announcements = announcementDataSource.getAnnouncements()
               call.respond(HttpStatusCode.OK, announcements)
           }catch (e:Exception){
               call.respond(HttpStatusCode.InternalServerError, "Announcements not found")
           }

       }

//        get(SEARCH_ANNOUNCEMENT){
//
//        }
   }
    authenticate("jwt"){
        get(GET_ANNOUNCEMENT_USER){
            try {
                val announcements = announcementDataSource.getAnnouncements()
                call.respond(HttpStatusCode.OK, announcements)
            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError, "Announcements not found")
            }

        }
    }

    authenticate("club-jwt"){
        get(GET_ANNOUNCEMENT_CLUB){
            try {
                val announcements = announcementDataSource.getAnnouncements()
                call.respond(HttpStatusCode.OK, announcements)
            }catch (e:Exception){
                call.respond(HttpStatusCode.InternalServerError, "Announcements not found")
            }

        }
    }

}
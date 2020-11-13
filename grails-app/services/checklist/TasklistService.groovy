package checklist

import com.webapp.cl.AppUtil
import com.webapp.cl.Globalconfig
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

@Transactional
class TasklistService {

    def serviceMethod() {

    }

    def save(GrailsParameterMap params){
        Tasklist tasklist= new Tasklist(params)
        def response= AppUtil.saveResponse(false,tasklist)
        if(tasklist.validate()){
            tasklist.save(flush:true)
            if(!tasklist.hasErrors()){
                response.isSuccess=true
            }
        }else{
            tasklist.errors.allErrors.each {
                println(it)
            }
        }
        return response
    }

    def update(Tasklist tasklist, GrailsParameterMap params){
        tasklist.properties=params
        def response=AppUtil.saveResponse(false,tasklist)
        if(tasklist.validate()){
            tasklist.save(flush:true)
            if(!tasklist.hasErrors()){
                response.isSuccess=true
            }
        }
        return response
    }
    def getbyId(Serializable id){ //To get Member by Id
        return Task.get(id)
    }
    def list(GrailsParameterMap params){
        params.max= params.max ?: Globalconfig.itemsPerpage()
        List<Tasklist> listtask = Tasklist.createCriteria().list(params){
            if (params?.colName && params?.colValue) {
                like(params.colName, "%" + params.colValue + "%")
            }
            if (!params.sort) {
                order("id", "desc")
            }
        }
        return [list:listtask,count:Tasklist.count()]
    }
    def cleanTasklistbyId(Integer id){
        Tasklist taskgroup = Tasklist.get(id)
        taskgroup.task.each {task ->
            task.removeFromTasklist(taskgroup)
        }
        taskgroup.save(flush:true)
    }
    def getGroupList(){
        return Tasklist.createCriteria().list {

            eq("Task",Task)

        }
    }

    def delete(Tasklist tasklist){
        tasklist.delete(flush: true)

        return true
    }

}

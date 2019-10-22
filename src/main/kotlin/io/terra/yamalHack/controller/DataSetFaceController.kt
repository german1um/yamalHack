package io.terra.yamalHack.controller

import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.dto.PersonGroupDto
import io.terra.yamalHack.service.DataSetFaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dataset")
class DataSetFaceController(
        @Autowired private val dataSetFaceService: DataSetFaceService
) {

    @PostMapping("/createPersonGroup")
    fun createPersonGroup(
            @RequestParam("name") name: String,
            @RequestParam(required = false, value = "userData") userData: String?
    ): String {
        return dataSetFaceService.createPersonGroup(PersonGroupDto(name, userData ?: ""))
    }

    @PostMapping("/createPersonInGroup")
    fun createPersonInGroup(
            @RequestParam("groupId") groupId: String,
            @RequestParam("name") name: String,
            @RequestParam(required = false, value = "userData") userData: String?
    ): String {
        return dataSetFaceService.createPersonInGroup(groupId, PersonDto(name, userData ?: ""))
    }

    @PostMapping("/addFaceToPerson")
    fun addFaceToPerson(
            @RequestParam("groupId") groupId: String,
            @RequestParam("personId") personId: String,
            @RequestParam("imageUrl") imageUrl: String
    ): String {
        return dataSetFaceService.addFaceToPerson(groupId, personId, imageUrl)
    }

    @PostMapping("/train")
    fun trainPersonGroup(
            @RequestParam("groupId") groupId: String
    ): String {
        return dataSetFaceService.trainPersonGroup(groupId)
    }

    @GetMapping("/trainingStatus")
    fun trainingStatus(
            @RequestParam("groupId") groupId: String
    ): String{
        return dataSetFaceService.trainingStatus(groupId)
    }
}
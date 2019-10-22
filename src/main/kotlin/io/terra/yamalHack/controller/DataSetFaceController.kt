package io.terra.yamalHack.controller

import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.dto.PersonGroupDto
import io.terra.yamalHack.service.DataSetFaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dataset")
class DataSetFaceController(
        @Autowired private val datasetFaceService: DataSetFaceService
) {

    @PostMapping("/createPersonGroup")
    fun createPersonGroup(
            @RequestParam("name") name: String,
            @RequestParam(required = false, value = "userData") userData: String?
    ): String {
        return datasetFaceService.createPersonGroup(PersonGroupDto(name, userData ?: ""))
    }

    @PostMapping("/createPersonInGroup")
    fun createPersonInGroup(
            @RequestParam("groupId") groupId: String,
            @RequestParam("name") name: String,
            @RequestParam(required = false, value = "userData") userData: String?
    ): String {
        return datasetFaceService.createPersonInGroup(groupId, PersonDto(name, userData ?: ""))
    }

    @PostMapping("/addFaceToPerson")
    fun addFaceToPerson(
            @RequestParam("groupId") groupId: String,
            @RequestParam("personId") personId: String,
            @RequestParam("imageUrl") imageUrl: String
    ): String {
        return datasetFaceService.addFaceToPerson(groupId, personId, imageUrl)
    }
}
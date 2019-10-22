package io.terra.yamalHack.controller

import io.terra.yamalHack.dto.ChildStatsDto
import io.terra.yamalHack.model.Parent
import io.terra.yamalHack.model.Person
import io.terra.yamalHack.service.ParentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/parent")
class ParentController(
        @Autowired val parentService: ParentService
) {

    @PostMapping("/login")
    fun login(
            @RequestParam("token") token: String
    ): Parent {
        return parentService.login(token)
    }

    @PostMapping("/{parentToken}/addChild")
    fun addChild(
            @PathVariable("parentToken") token: String,
            @RequestParam("childName") childName: String
    ): Parent {
        return parentService.addChild(token, childName)
    }

    @PostMapping("/{parentToken}/addChildrenPhotos")
    fun addChildrenPhotos(
            @PathVariable("parentToken") token: String,
            @RequestParam("childId") childId: String,
            @RequestParam("photoUrls") photoUrls: List<String>
    ): String {
        return parentService.addChildPhotos(token, childId, photoUrls)
    }

    @GetMapping("/{parentToken}/childStats")
    fun childStats(
            @PathVariable("parentToken") token: String,
            @RequestParam("childId") childId: String
    ): ChildStatsDto {
        return parentService.childStats(token, childId)
    }

}
package io.terra.yamalHack.service

import io.terra.yamalHack.api.entity.average
import io.terra.yamalHack.dto.ChildStatsDto
import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.model.Parent
import io.terra.yamalHack.repository.ParentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ParentService(
        @Autowired val parentRepository: ParentRepository,
        @Autowired val dataSetFaceService: DataSetFaceService,
        @Autowired val personGroupService: PersonGroupService,
        @Autowired val statsRegistratorService: StatsRegistratorService
) {

    fun login(token: String): Parent {
        return if (isExist(token)) {
            parentRepository.findByToken(token).get()
        } else {
            val newParent = Parent(token)
            save(newParent)
            newParent
        }
    }

    fun addChild(token: String, childName: String): Parent {
        val parent = login(token)
        val childId = dataSetFaceService.addPersonToGroup(tmpGroupId, PersonDto(childName))

        parentRepository.save(
                Parent(
                        id = parent.id,
                        token = parent.token,
                        name = parent.name,
                        childrenIds = parent.childrenIds + childId
                )
        )

        return parentRepository.findByToken(token).get()
    }

    fun addChildPhotos(token: String, childId: String, photoUrls: List<String>): String {
        val parent = login(token)
        parent.childrenIds.firstOrNull { it == childId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Child not found!")

        photoUrls.forEach { url ->
            dataSetFaceService.addFaceToPerson(tmpGroupId, childId, url)
        }

        return "OK"
    }

    fun isExist(token: String): Boolean {
        return parentRepository.findByToken(token).isPresent
    }

    fun save(parent: Parent) {
        parentRepository.save(parent)
    }

    fun childStats(token: String, childId: String): ChildStatsDto {
        val child = personGroupService.loadOrThrow(tmpGroupId).persons.first { it.id == childId }

        val emotions = statsRegistratorService.loadEmotionsForChild(child.id)
                .map { it.emotions }
                .average()
        /*val friendsStats = statsRegistratorService.loadFriendsStats().map { it.children.contains(childId) }
        val actionStats = statsRegistratorService.loadActionStats(childId)*/

        return ChildStatsDto(
                "",
                child.name,
                "Первый отряд",
                actionDuration = 100,
                energy = 100,
                distance = 100,
                rating = 100,
                actionTimetable = emptyList(),
                emotions = emotions,
                bestFriends = emptyList()
        )
    }

    companion object {
        const val tmpGroupId = "58fe9b26-7a45-48cc-9631-c5ab4a494b22"
    }
}
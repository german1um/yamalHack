package io.terra.yamalHack.service

import io.terra.yamalHack.cdn.GcsUploader
import io.terra.yamalHack.api.entity.average
import io.terra.yamalHack.cdn.GcsResolver
import io.terra.yamalHack.dto.BestFriends
import io.terra.yamalHack.dto.ChildStatsDto
import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.model.ChildDto
import io.terra.yamalHack.model.Parent
import io.terra.yamalHack.repository.ParentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class ParentService(
        @Autowired val parentRepository: ParentRepository,
        @Autowired val dataSetFaceService: DataSetFaceService,
        @Autowired val personGroupService: PersonGroupService,
        @Autowired val statsRegistratorService: StatsRegistratorService,
        @Autowired val gcsResolver: GcsResolver,
        @Autowired val gcsUploader: GcsUploader
) {

    val names = listOf("Gordon Friman", "Andrew Dorofeev", "Maks Kacal", "Ded Moroz", "Petr")

    fun login(token: String): Parent {
        /*if (isExist(token)) {
            parentRepository.findByToken(token).get()
        } else {
            val newParent = Parent(token)
            save(newParent)
        }*/

        if (!isExist(token)) {
            val newParent = Parent(token)
            save(newParent)
            addChild(token, names.shuffled().first())
        }

        return parentRepository.findByToken(token).get()
    }

    fun addChild(token: String, childName: String): Parent {
        val parent = parentRepository.findByToken(token).get()
        val childId = dataSetFaceService.addPersonToGroup(GroupData.tmpGroupId, PersonDto(childName))

        parentRepository.save(
                Parent(
                        id = parent.id,
                        token = parent.token,
                        name = parent.name,
                        children = parent.children + ChildDto(childId, 0)
                )
        )

        return parentRepository.findByToken(token).get()
    }

    fun addChildPhotos(token: String, childId: String, photoUrls: List<String>): String {
        val parent = login(token)
        parent.children.firstOrNull { it.id == childId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Child not found!")

        photoUrls.forEach { url ->
            dataSetFaceService.addFaceToPerson(GroupData.tmpGroupId, childId, url)
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
        val child = personGroupService.loadOrThrow(GroupData.tmpGroupId).persons.firstOrNull { it.id == childId } ?: return ChildStatsDto.fuckYura()

        val emotions = statsRegistratorService.loadEmotionsForChild(child.id)
                .map { it.emotions }
                .average()

        val friends = listOf(BestFriends.getTestFriends(100), BestFriends.getTestFriends(43), BestFriends.getTestFriends(766))
        /*val actionStats = statsRegistratorService.loadActionStats(childId)*/

        return ChildStatsDto(
                loadChildProfilePhoto(child.id),
                child.name,
                "Первый отряд",
                actionDuration = 60*62*1332,
                energy = 5327,
                distance = 7566,
                rating = 100,
                actionTimetable = listOf(2,3,1),
                emotions = emotions,
                bestFriends = friends
        )
    }

    fun addChildPhoto(token: String, childId: String, file: MultipartFile): String {
        val parent = login(token)
        parent.children.firstOrNull { it.id == childId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Child not found!")


        val links = gcsResolver.generateContentLinks(UUID.randomUUID().toString())
        gcsUploader.loadImageToGcs(links.uploadLink, file)

        dataSetFaceService.addFaceToPerson(GroupData.tmpGroupId, childId, links.downloadLink)

        incrementChildPhotoCount(token, childId)

        return links.downloadLink
    }

    private fun incrementChildPhotoCount(token: String, childId: String) {
        val parent = login(token)
        val children = parent.children.toMutableList()
        val oldCount = children.find { it.id == childId }?.photoCount ?: return
        children.removeIf { it.id == childId }
        val newChild = ChildDto(childId, oldCount + 1)
        children.add(newChild)

        parentRepository.save(
                Parent(
                        token = parent.token,
                        name = parent.name,
                        children = children,
                        id = parent.id
                )
        )

    }

    private fun loadChildProfilePhoto(childId: String): String {
        return personGroupService.loadOrThrow(GroupData.tmpGroupId).persons.first { it.id == childId }.persistedFaces.first().url
    }
}


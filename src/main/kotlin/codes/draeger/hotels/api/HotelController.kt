package codes.draeger.hotels.api

import codes.draeger.hotels.model.*
import codes.draeger.hotels.repository.DummyHotelRepository
import codes.draeger.hotels.repository.HotelRepository
import codes.draeger.hotels.repository.ReviewRepository
import codes.draeger.hotels.repository.enties.HotelEntity
import codes.draeger.hotels.repository.enties.ReviewEntity
import codes.draeger.hotels.repository.enties.toHotelEntity
import codes.draeger.hotels.service.HotelService
import org.springframework.web.bind.annotation.*

@RestController
class HotelController(
    private val dummyHotelRepository: DummyHotelRepository,
    private val hotelRepository: HotelRepository,
    private val reviewRepository: ReviewRepository,
    private val hotelService: HotelService,
) {
    @PostMapping("/add")
    fun addData(
        @RequestBody body: Hotel
    ) {
        hotelService.addHotel(body)
    }

    @GetMapping("/all")
    fun getAllHotels(): List<HotelResponse> = hotelRepository.findAll().map { it.toHotelResponse() }

    @GetMapping("/all/{id}")
    fun getOneHotel(@PathVariable id: Int): HotelResponse = hotelRepository.findById(id).get().toHotelResponse()

    @DeleteMapping("/remove/{id}")
    fun removeHotel(
        @PathVariable id: Int
    ) {
        hotelRepository.deleteById(id)
    }

    @PostMapping("/add-review/{id}")
    fun addReview(
        @PathVariable id: Int,
        @RequestBody body: Review
    ) {
        reviewRepository.save(
            ReviewEntity(
                message = body.message,
                stars = body.stars,
                hotelId = id
            )
        )
    }

    @PostMapping("/add-room/{id}")
    fun addRoom(
        @PathVariable id: Int,
        @RequestBody body: Int
    ) {
        // TODO
    }

    @PatchMapping("/all/{hotelId}/change-status/{reviewId}")
    fun changeRoomStatus(
        @PathVariable hotelId: String,
        @PathVariable reviewId: Int,
        @RequestBody body: RoomStatus
    ) {
        // TODO
    }
}

fun HotelEntity.toHotelResponse() = HotelResponse(
    id = id.toString(),
    name = name,
    address = HotelResponse.Address(
        street = address.street,
        number = address.number,
        zipCode = address.zipCode,
        city = address.city
    ),
    reviews = reviews,
    rooms = emptyList() // TODO
)

data class HotelResponse(
    val id: String,
    val name: String,
    val address: Address,
    var reviews: Set<ReviewEntity>,
    var rooms: List<Room>
) {
    data class Address(
        val street: String,
        val number: String,
        val zipCode: String,
        val city: String
    )
}
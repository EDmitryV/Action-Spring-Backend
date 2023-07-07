package com.actiongroup.actionserver.controllers.events;

import com.actiongroup.actionserver.services.events.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

//    @GetMapping("/get-on-map")
//    ResponseEntity<List<Event>> getOnMap(
//            @RequestParam(value = "startPoint[]") Double[] startPoint,
//            @RequestParam(value = "endPoint[]") Double[] endPoint,
//            @RequestParam(value = "date_and_time") String dateAndTimeFilter,
//            @RequestParam(value = "from_me") Boolean fromMe,
//            @RequestParam(value = "from_subscriptions") Boolean fromSubscriptions,
//            @RequestParam(value = "from_friends") Boolean fromFriends,
//            @RequestParam(value = "filter_categories[]") String[] filterCategories
//    ) {
//        GeometryFactory geometryFactory = new GeometryFactory();
//        List<String> possibleDateTimeFilters = Arrays.asList("now", "today", "week", "month");
//        if (startPoint.length != 2 || endPoint.length != 2 || (!possibleDateTimeFilters.contains(dateAndTimeFilter) && !isValidFormat("dd-MM-yyyy", dateAndTimeFilter))) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<List<Event>>(eventService.getEventsOnMap(
//                geometryFactory.createPoint(new Coordinate(startPoint[0], startPoint[1])),
//                geometryFactory.createPoint(new Coordinate(endPoint[0], endPoint[1])),
//                dateAndTimeFilter,
//                fromMe,
//                fromSubscriptions,
//                fromFriends,
//                filterCategories
//                ), HttpStatus.OK);
//    }
}
package voltskiya.mob.system.base.selector.api;

import apple.voltskiya.webroot.api.base.ApiController;
import apple.voltskiya.webroot.session.AppRole;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import java.util.Map;
import java.util.regex.Pattern;
import voltskiya.mob.system.base.selector.SpawnSelectorDatabase;

public class SpawnSelectorController extends ApiController {

    private static final Pattern SELECTOR_NAME_REGEX = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{7,29}$");


    public SpawnSelectorController() {
        super("/mob/spawn-controller");
    }

    @Override
    public void register(Javalin app) {
        app.post(path("/add"), this::addSelector, AppRole.GAMEMASTER);
    }

    private void addSelector(Context ctx) {
        String name = ctx.bodyValidator(SpawnSelectorAddRequest.class).get().name;
        if (name == null || !SELECTOR_NAME_REGEX.asMatchPredicate().test(name))
            throw new HttpResponseException(HttpStatus.BAD_REQUEST, "'name' is invalid. %s was provided".formatted(name), Map.of());
        SpawnSelectorDatabase.add(name);

    }
}

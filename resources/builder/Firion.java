package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Firion extends _Default{
	public Firion() {
		super("Firion", "frioniel");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6254};
				break;
			case HP:
				ids = new int[]{6255};
				break;
			case S1:
				ids = new int[]{3603, 3604};
				break;
			case S2:
				ids = new int[]{3607, 3666, 3608};
				break;
			case EX:
				ids = new int[]{6013, 6012};
				break;
			case AA:
				ids = new int[]{6017};
				break;
			case LD:
				ids = new int[]{10353};
				break;
			case BT:
				ids = new int[]{10355};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}
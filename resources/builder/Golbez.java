package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Golbez extends _Default{
	public Golbez() {
		super("Golbez", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3569};
				break;
			case HP:
				ids = new int[]{6234};
				break;
			case S1:
				ids = new int[]{3555, 3553, 3554};
				break;
			case S2:
				ids = new int[]{3567, 3565, 3566};
				break;
			case EX:
				ids = new int[]{6232};
				break;
			case AA:
				ids = new int[]{6219};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
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
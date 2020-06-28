package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Warrior_of_Light extends _Default{
	public Warrior_of_Light() {
		super("Warrior of Light", "wol");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{10025, 10026};
				break;
			case HP:
				ids = new int[]{10027, 10028};
				break;
			case S1:
				ids = new int[]{4121};
				break;
			case S2:
				ids = new int[]{4123};
				break;
			case EX:
				ids = new int[]{5124};
				break;
			case AA:
				ids = new int[]{3925};
				break;
			case LD:
				ids = new int[]{9383};
				break;
			case BT:
				ids = new int[]{9384};
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
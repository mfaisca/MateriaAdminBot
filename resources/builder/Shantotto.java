package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Shantotto extends _Default{
	public Shantotto() {
		super("Shantotto", "totto");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6692};
				break;
			case HP:
				ids = new int[]{6693};
				break;
			case S1:
				ids = new int[]{4059, 4060};
				break;
			case S2:
				ids = new int[]{4064, 4063};
				break;
			case EX:
				ids = new int[]{6691, 6690};
				break;
			case AA:
				ids = new int[]{4039};
				break;
			case LD:
				ids = new int[]{8855};
				break;
			case BT:
				ids = new int[]{9160};
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
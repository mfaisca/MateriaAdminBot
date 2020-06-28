package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Squall extends _Default{
	public Squall() {
		super("Squall", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4079};
				break;
			case HP:
				ids = new int[]{8916};
				break;
			case S1:
				ids = new int[]{4074, 4073};
				break;
			case S2:
				ids = new int[]{4078, 4077};
				break;
			case EX:
				ids = new int[]{5386};
				break;
			case AA:
				ids = new int[]{5390};
				break;
			case LD:
				ids = new int[]{8529};
				break;
			case BT:
				ids = new int[]{8914};
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
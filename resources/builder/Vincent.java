package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vincent extends _Default{
	public Vincent() {
		super("Vincent", "vinc", "vinc", "vinc", "vinc", "vinc", "vinc", "vinc", "vinc");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5070};
				break;
			case HP:
				ids = new int[]{5126};
				break;
			case S1:
				ids = new int[]{5064, 5063};
				break;
			case S2:
				ids = new int[]{5068, 5067};
				break;
			case EX:
				ids = new int[]{5146};
				break;
			case AA:
				ids = new int[]{5033};
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